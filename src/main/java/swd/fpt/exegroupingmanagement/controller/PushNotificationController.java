package swd.fpt.exegroupingmanagement.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.RegisterDeviceTokenRequest;
import swd.fpt.exegroupingmanagement.dto.request.SendNotificationRequest;
import swd.fpt.exegroupingmanagement.dto.response.DeviceTokenResponse;
import swd.fpt.exegroupingmanagement.dto.response.NotificationResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;
import swd.fpt.exegroupingmanagement.service.PushNotificationService;

@RestController
@RequestMapping("/api/notifications")
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Push Notification Management", description = "APIs for managing push notifications")
public class PushNotificationController {
    
    PushNotificationService pushNotificationService;

    @PostMapping("/register-token")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Register device token for push notifications",
               description = "Mobile app calls this API after successful login to register FCM device token")
    public ResponseEntity<StandardResponse<Object>> registerDeviceToken(
            @Valid @RequestBody RegisterDeviceTokenRequest request) {
        
        DeviceTokenResponse result = pushNotificationService.registerDeviceToken(request);
        return ResponseEntity.ok(success("Đăng ký device token thành công", result));
    }

    @DeleteMapping("/remove-token")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Remove device token (on logout)",
               description = "Mobile app calls this API when user logs out to stop receiving notifications")
    public ResponseEntity<StandardResponse<String>> removeDeviceToken(
            @RequestParam String token) {
        
        pushNotificationService.removeDeviceToken(token);
        return ResponseEntity.ok(success("Xóa device token thành công"));
    }
    

    @DeleteMapping("/remove-all-tokens")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Remove all device tokens of current user",
               description = "Logout from all devices")
    public ResponseEntity<StandardResponse<String>> removeAllDeviceTokens() {
        
        pushNotificationService.removeAllDeviceTokens();
        return ResponseEntity.ok(success("Đã xóa tất cả device tokens"));
    }
    
    @GetMapping("/my-devices")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get my registered devices",
               description = "Get list of devices that can receive push notifications")
    public ResponseEntity<StandardResponse<Object>> getMyDevices() {
        
        List<DeviceTokenResponse> result = pushNotificationService.getMyDeviceTokens();
        return ResponseEntity.ok(success(
            "Lấy danh sách thiết bị thành công (tìm thấy " + result.size() + " thiết bị)", 
            result));
    }

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Send push notification to multiple users",
               description = "Send notification to selected users.")
    public ResponseEntity<StandardResponse<Object>> sendNotification(
            @Valid @RequestBody SendNotificationRequest request) {
        
        NotificationResponse result = pushNotificationService
                .sendNotificationToMultipleUsers(request);
        
        String message = String.format(
            "Gửi notification hoàn tất: %d/%d người dùng nhận thành công",
            result.getSuccessCount(),
            result.getSuccessCount() + result.getFailureCount()
        );
        
        return ResponseEntity.ok(success(message, result));
    }
    
    /**
     * API để gửi notification đến TẤT CẢ users (broadcast)
     * Chỉ ADMIN mới có quyền
     * 
     * Use case:
     * - Thông báo bảo trì hệ thống
     * - Thông báo khẩn cấp
     * - Thông báo sự kiện quan trọng
     */
    @PostMapping("/broadcast")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Broadcast notification to all users (Admin only)",
               description = "Send notification to ALL users. Use carefully for important announcements only.")
    public ResponseEntity<StandardResponse<Object>> broadcastNotification(
            @RequestParam String title,
            @RequestParam String body) {
        
        int successCount = pushNotificationService.sendNotificationToAllUsers(title, body, null);
        
        String message = String.format("Đã gửi broadcast notification đến %d người dùng", successCount);
        
        return ResponseEntity.ok(success(message, 
            NotificationResponse.builder()
                .successCount(successCount)
                .build()));
    }
    
    /**
     * API TEST để gửi notification đến chính mình
     * Dùng để test xem notification có hoạt động không
     */
    @PostMapping("/test")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Send test notification to yourself",
               description = "Send a test notification to your own devices for testing purposes")
    public ResponseEntity<StandardResponse<Object>> sendTestNotification(
            @RequestParam(required = false, defaultValue = "Test Notification") String title,
            @RequestParam(required = false, defaultValue = "Đây là test notification từ EXE Grouping Management System") String body) {
        
        // Lấy danh sách devices để kiểm tra
        List<DeviceTokenResponse> myDevices = pushNotificationService.getMyDeviceTokens();
        
        if (myDevices.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(StandardResponse.error(
                        "Bạn chưa đăng ký device token nào. Vui lòng đăng ký device token trước."));
        }
        
        // Note: sendNotificationToUser sẽ tự động lấy userId từ getCurrentUser
        // nên không cần truyền userId vào
        // Tạm thời comment out vì cần refactor method
        
        return ResponseEntity.ok(success(
            String.format("Tìm thấy %d thiết bị đã đăng ký. " +
                         "Để test gửi notification, vui lòng dùng API /send với userId của bạn.", 
                         myDevices.size()),
            myDevices));
    }
}

