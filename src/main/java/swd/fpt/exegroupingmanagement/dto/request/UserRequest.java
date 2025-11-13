package swd.fpt.exegroupingmanagement.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.Gender;
import swd.fpt.exegroupingmanagement.enums.UserStatus;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự")
    String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    String password;
    
    @NotNull(message = "Role không được để trống")
    Long roleId;
    
    Long majorId;
    
    Gender gender;
    
    LocalDate dob;
    
    UserStatus status;
}
