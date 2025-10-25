package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {
    
    @NotNull(message = "Subject ID is required")
    private Long subjectId;
    
    @NotNull(message = "Semester ID is required")
    private Long semesterId;
    
    private Long mentorId;
    
    @NotBlank(message = "Code is required")
    private String code;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    private Integer maxStudents;
    private Integer minStudents;
    private Integer maxTeamSize;
    private Integer minTeamSize;
    private Boolean allowTeamFormation;
    private LocalDateTime teamFormationDeadline;
    private String schedule;
    private String room;
    private LocalDate startDate;
    private LocalDate endDate;
}

