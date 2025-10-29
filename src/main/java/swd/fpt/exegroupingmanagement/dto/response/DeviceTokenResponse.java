package swd.fpt.exegroupingmanagement.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.DeviceType;

/**
 * Response trả về thông tin device token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceTokenResponse {
    
    Long deviceTokenId;
    
    /**
     * Chỉ hiển thị 10 ký tự đầu và cuối của token vì lý do bảo mật
     */
    String tokenPreview;
    
    DeviceType deviceType;
    
    String deviceName;
    
    LocalDateTime lastUsedAt;
    
    Boolean isActive;
    
    LocalDateTime createdAt;
}

