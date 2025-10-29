package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MentorProfileRequest {
    
    @NotBlank(message = "Tên viết tắt không được để trống")
    @Size(max = 50, message = "Tên viết tắt không được quá 50 ký tự")
    String shortName;
    
    @NotNull(message = "ID người dùng không được để trống")
    Long userId;
}

