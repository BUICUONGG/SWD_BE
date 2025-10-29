package swd.fpt.exegroupingmanagement.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRequest {
    
    @NotBlank(message = "Mã lớp không được để trống")
    @Size(max = 20, message = "Mã lớp không được quá 20 ký tự")
    String code;
    
    @NotBlank(message = "Tên lớp không được để trống")
    @Size(max = 200, message = "Tên lớp không được quá 200 ký tự")
    String name;
    
    @Min(value = 1, message = "Số sinh viên tối đa phải lớn hơn 0")
    Integer maxStudents;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime teamFormationDeadline;
    
    CourseStatus status;
    
    Long mentorId;
    
    @NotNull(message = "Môn học không được để trống")
    Long subjectId;
    
    @NotNull(message = "Kỳ học không được để trống")
    Long semesterId;
}

