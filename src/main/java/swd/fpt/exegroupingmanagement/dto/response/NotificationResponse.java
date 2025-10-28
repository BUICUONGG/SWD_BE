package swd.fpt.exegroupingmanagement.dto.response;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {

    int successCount;

    int failureCount;
    
    /**
     * Chi tiết kết quả gửi cho từng user
     * Key: userId, Value: "success" hoặc error message
     */
    Map<Long, String> details;
    
    /**
     * Danh sách các device tokens không hợp lệ (đã expired hoặc bị xóa)
     * Cần deactivate các tokens này
     */
    List<String> invalidTokens;
}

