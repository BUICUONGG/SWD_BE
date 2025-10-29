package swd.fpt.exegroupingmanagement.service;

import java.util.List;
import java.util.Map;

import swd.fpt.exegroupingmanagement.dto.request.RegisterDeviceTokenRequest;
import swd.fpt.exegroupingmanagement.dto.request.SendNotificationRequest;
import swd.fpt.exegroupingmanagement.dto.response.DeviceTokenResponse;
import swd.fpt.exegroupingmanagement.dto.response.NotificationResponse;


public interface PushNotificationService {
    
    DeviceTokenResponse registerDeviceToken(RegisterDeviceTokenRequest request);

    void removeDeviceToken(String token);

    void removeAllDeviceTokens();
    
    List<DeviceTokenResponse> getMyDeviceTokens();
    
    int sendNotificationToUser(Long userId, String title, String body, Map<String, String> data);

    NotificationResponse sendNotificationToMultipleUsers(SendNotificationRequest request);

    int sendNotificationToAllUsers(String title, String body, Map<String, String> data);
}

