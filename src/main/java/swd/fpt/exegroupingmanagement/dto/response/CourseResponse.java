package swd.fpt.exegroupingmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    
    private Long courseId;
    private Long subjectId;
    private String subjectName;
    private Long semesterId;
    private String semesterName;
    private Long mentorId;
    private String mentorName;
    private String code;
    private String name;
    private String description;
    private Integer maxStudents;
    private Integer currentStudents;
    private Integer minStudents;
    private Integer maxTeamSize;
    private Integer minTeamSize;
    private Boolean allowTeamFormation;
    private LocalDateTime teamFormationDeadline;
    private String schedule;
    private String room;
    private LocalDate startDate;
    private LocalDate endDate;
    private CourseStatus status;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

