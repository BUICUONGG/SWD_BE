package swd.fpt.exegroupingmanagement.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentResponse {
    Long enrollmentId;
    LocalDateTime enrollmentDate;
    Boolean isDeleted;

    // Related entities
    Long userId;
    String userEmail;
    String userFullName;
    Long courseId;
    String courseCode;
    String courseName;
}

