package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectRequest {
    
    @NotBlank(message = "Mã môn học không được để trống")
    @Size(max = 20, message = "Mã môn học không được quá 20 ký tự")
    String code;
    
    @NotBlank(message = "Tên môn học không được để trống")
    @Size(max = 200, message = "Tên môn học không được quá 200 ký tự")
    String name;
    
    String prerequisiteCodes; // "SWE201a,SWE201b"
}

