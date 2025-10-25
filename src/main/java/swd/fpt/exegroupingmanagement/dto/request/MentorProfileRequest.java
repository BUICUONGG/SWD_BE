package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MentorProfileRequest {
    
    @NotBlank(message = "Mã giảng viên không được để trống")
    @Size(max = 20, message = "Mã giảng viên không được quá 20 ký tự")
    String employeeCode;
    
    @Min(value = 1, message = "Số lớp tối đa phải lớn hơn 0")
    Integer maxCoursesPerSemester;
    
    @NotNull(message = "ID người dùng không được để trống")
    Long userId;
}

