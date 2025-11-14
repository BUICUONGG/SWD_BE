package swd.fpt.exegroupingmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.dto.request.RegisterDeviceTokenRequest;
import swd.fpt.exegroupingmanagement.dto.request.SendNotificationRequest;
import swd.fpt.exegroupingmanagement.dto.response.DeviceTokenResponse;
import swd.fpt.exegroupingmanagement.dto.response.NotificationResponse;
import swd.fpt.exegroupingmanagement.entity.DeviceTokenEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.repository.DeviceTokenRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import swd.fpt.exegroupingmanagement.service.PushNotificationService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
public class PushNotificationServiceImpl implements PushNotificationService {

    FirebaseMessaging firebaseMessaging;
    DeviceTokenRepository deviceTokenRepository;
    UserRepository userRepository;

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public DeviceTokenResponse registerDeviceToken(RegisterDeviceTokenRequest request) {
        UserEntity currentUser = getCurrentUser();
        Optional<DeviceTokenEntity> existingToken = deviceTokenRepository.findByToken(request.getToken());

        DeviceTokenEntity deviceToken;
        if (existingToken.isPresent()) {
            // Nếu token đã tồn tại, cập nhật thông tin
            deviceToken = existingToken.get();
            deviceToken.setUser(currentUser);
            deviceToken.setDeviceType(request.getDeviceType());
            deviceToken.setDeviceName(request.getDeviceName());
            deviceToken.setLastUsedAt(LocalDateTime.now());
            deviceToken.setIsActive(true);
            log.info("Updated existing device token");
        } else {
            deviceToken = DeviceTokenEntity.builder()
                    .token(request.getToken())
                    .user(currentUser)
                    .deviceType(request.getDeviceType())
                    .deviceName(request.getDeviceName())
                    .lastUsedAt(LocalDateTime.now())
                    .isActive(true)
                    .build();

            log.info("Created new device token");
        }
        log.info("lưu token");
        deviceToken = deviceTokenRepository.save(deviceToken);
        return mapToResponse(deviceToken);
    }

    @Override
    @Transactional
    public void removeDeviceToken(String token) {
        deviceTokenRepository.deactivateToken(token);
    }

    @Override
    @Transactional
    public void removeAllDeviceTokens() {
        UserEntity currentUser = getCurrentUser();
        deviceTokenRepository.deactivateAllUserTokens(currentUser.getUserId());
    }

    @Override
    public List<DeviceTokenResponse> getMyDeviceTokens() {
        UserEntity currentUser = getCurrentUser();

        List<DeviceTokenEntity> tokens = deviceTokenRepository
                .findActiveTokensByUserId(currentUser.getUserId());

        return tokens.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int sendNotificationToUser(Long userId, String title, String body, Map<String, String> data) {
        // Lấy tất cả device tokens của user
        List<DeviceTokenEntity> tokens = deviceTokenRepository.findActiveTokensByUserId(userId);
        log.info(tokens.toString(), "tokens");
        if (tokens.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        List<String> invalidTokens = new ArrayList<>();

        // Gửi notification đến từng device
        for (DeviceTokenEntity tokenEntity : tokens) {
            try {
                sendToDevice(tokenEntity.getToken(), title, body, null, data);
                successCount++;
                // Cập nhật last used time
                deviceTokenRepository.updateLastUsedAt(tokenEntity.getToken(), LocalDateTime.now());

            } catch (FirebaseMessagingException e) {
                log.error("Failed to send notification to token: {}",
                        maskToken(tokenEntity.getToken()), e);
                // Nếu token không hợp lệ, đánh dấu để xóa
                if (isInvalidTokenError(e)) {
                    invalidTokens.add(tokenEntity.getToken());
                }
            }
        }
        // Deactivate invalid tokens
        if (!invalidTokens.isEmpty()) {
            invalidTokens.forEach(deviceTokenRepository::deactivateToken);
        }

        return successCount;
    }

    @Override
    @Transactional
    public NotificationResponse sendNotificationToMultipleUsers(SendNotificationRequest request) {
        int totalSuccess = 0;
        int totalFailure = 0;
        Map<Long, String> details = new HashMap<>();
        List<String> allInvalidTokens = new ArrayList<>();

        for (Long userId : request.getUserIds()) {
            try {
                if (!userRepository.existsById(userId)) {
                    details.put(userId, "User not found");
                    totalFailure++;
                    continue;
                }

                int successCount = sendNotificationToUser(
                        userId,
                        request.getTitle(),
                        request.getBody(),
                        request.getData()
                );

                if (successCount > 0) {
                    details.put(userId, "Success (" + successCount + " devices)");
                    totalSuccess++;
                } else {
                    details.put(userId, "No active devices");
                    totalFailure++;
                }

            } catch (Exception e) {
                details.put(userId, "Error: " + e.getMessage());
                totalFailure++;
            }
        }

        return NotificationResponse.builder()
                .successCount(totalSuccess)
                .failureCount(totalFailure)
                .details(details)
                .invalidTokens(allInvalidTokens)
                .build();
    }

    @Override
    public int sendNotificationToAllUsers(String title, String body, Map<String, String> data) {
        log.info("Sending broadcast notification to all users");

        // Lấy tất cả user IDs
        List<Long> allUserIds = userRepository.findAll().stream()
                .map(UserEntity::getUserId)
                .collect(Collectors.toList());

        if (allUserIds.isEmpty()) {
            log.warn("No users found to send notification");
            return 0;
        }

        // Sử dụng method gửi đến nhiều users
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userIds(allUserIds)
                .title(title)
                .body(body)
                .data(data)
                .build();

        NotificationResponse response = sendNotificationToMultipleUsers(request);

        log.info("Broadcast notification sent: {}/{} users successful",
                response.getSuccessCount(), allUserIds.size());

        return response.getSuccessCount();
    }

    /**
     * Gửi notification đến một device cụ thể
     */
    private void sendToDevice(String token, String title, String body,
            String imageUrl, Map<String, String> data)
            throws FirebaseMessagingException {

        // Build notification
        Notification.Builder notificationBuilder = Notification.builder()
                .setTitle(title)
                .setBody(body);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            notificationBuilder.setImage(imageUrl);
        }

        // Build message
        Message.Builder messageBuilder = Message.builder()
                .setToken(token)
                .setNotification(notificationBuilder.build());

        // Add custom data if present
        if (data != null && !data.isEmpty()) {
            messageBuilder.putAllData(data);
        }

        // Configure Android-specific options
        messageBuilder.setAndroidConfig(AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder()
                        .setSound("default")
                        .setColor("#FF0000") // Màu icon notification
                        .build())
                .build());

        // Configure iOS-specific options
        messageBuilder.setApnsConfig(ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setSound("default")
                        .setBadge(1) // Số badge trên icon app
                        .build())
                .build());

        Message message = messageBuilder.build();

        // Gửi message
        String response = firebaseMessaging.send(message);
        log.debug("Successfully sent notification: {}", response);
    }

    /**
     * Kiểm tra xem error có phải do token không hợp lệ không
     */
    private boolean isInvalidTokenError(FirebaseMessagingException e) {
        String errorCode = e.getMessagingErrorCode().name();
        return errorCode.equals("INVALID_ARGUMENT")
                || errorCode.equals("UNREGISTERED")
                || errorCode.equals("INVALID_REGISTRATION");
    }

    /**
     * Map entity sang response DTO
     */
    private DeviceTokenResponse mapToResponse(DeviceTokenEntity entity) {
        return DeviceTokenResponse.builder()
                .deviceTokenId(entity.getDeviceTokenId())
                .tokenPreview(maskToken(entity.getToken()))
                .deviceType(entity.getDeviceType())
                .deviceName(entity.getDeviceName())
                .lastUsedAt(entity.getLastUsedAt())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    /**
     * Mask token để bảo mật (chỉ hiện 10 ký tự đầu và cuối)
     */
    private String maskToken(String token) {
        if (token == null || token.length() <= 20) {
            return "***";
        }
        return token.substring(0, 10) + "..." + token.substring(token.length() - 10);
    }
}
