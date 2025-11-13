package swd.fpt.exegroupingmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse  {
    Long courseId;
    String code;
    String name;
    Integer maxStudents;
    Integer currentStudents;
    LocalDateTime teamFormationDeadline;
    CourseStatus status;
    Boolean isDeleted;

    // Related entities
    Long mentorId;
    String mentorName;
    Long subjectId;
    String subjectCode;
    Long semesterId;
    String semesterCode;
}
