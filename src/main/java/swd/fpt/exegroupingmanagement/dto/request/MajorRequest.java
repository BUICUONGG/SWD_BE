package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MajorRequest {
    
    @NotBlank(message = "Mã chuyên ngành không được để trống")
    @Size(max = 20, message = "Mã chuyên ngành không được quá 20 ký tự")
    String code;
    
    @NotBlank(message = "Tên chuyên ngành không được để trống")
    @Size(max = 200, message = "Tên chuyên ngành không được quá 200 ký tự")
    String name;
    
    Boolean isActive;
}

