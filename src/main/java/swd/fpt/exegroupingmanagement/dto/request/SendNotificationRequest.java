package swd.fpt.exegroupingmanagement.dto.request;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Request để gửi push notification
 * Admin hoặc hệ thống sẽ sử dụng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendNotificationRequest {
    
    @NotNull(message = "User IDs không được null")
    List<Long> userIds;
   
    @NotBlank(message = "Title không được để trống")
    String title;
    
    @NotBlank(message = "Body không được để trống")
    String body;
    
    String imageUrl;
    
    Map<String, String> data;
}

