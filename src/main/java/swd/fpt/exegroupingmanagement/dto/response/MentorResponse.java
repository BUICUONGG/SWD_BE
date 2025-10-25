package swd.fpt.exegroupingmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorResponse {
    
    private Long mentorProfileId;
    private Long userId;
    private String email;
    private String fullName;
    private String phone;
    private String avatarUrl;
    
    // Mentor profile
    private String employeeCode;
    private String title;
    private String position;
    private String degree;
    private String department;
    private String faculty;
    private String officeLocation;
    private String officeHours;
    
    private Integer currentCourseCount;
    private Integer currentTeamCount;
    private Integer maxCoursesPerSemester;
    private Integer maxTeamsToMentor;
    
    private BigDecimal averageRating;
    private Integer totalReviews;
    
    private Boolean isAvailable;
    private Boolean isActive;
}

