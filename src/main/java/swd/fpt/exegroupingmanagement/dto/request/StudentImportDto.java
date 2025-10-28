package swd.fpt.exegroupingmanagement.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.Gender;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentImportDto {
    String email;
    String fullName;
    String password;
    Gender gender;
    LocalDate dob;
    Long roleId; // Default to STUDENT role
}

