package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentRequest {
    
    @NotNull(message = "ID sinh viên không được để trống")
    Long userId;
    
    @NotNull(message = "ID lớp không được để trống")
    Long courseId;
}

