package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.Gender;
import swd.fpt.exegroupingmanagement.validator.ConfirmPasswordConstraint;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfirmPasswordConstraint
public class RegisterRequest {
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email format is invalid")
    String email;

    @NotBlank(message = "Full name must not be blank")
    String fullName;

    LocalDate dob;

    Gender gender;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 4, message = "Password must be at least 5 characters")
    String password;

    String confirmPassword;
}
