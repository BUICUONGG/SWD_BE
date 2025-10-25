package swd.fpt.exegroupingmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorDetailResponse {
    
    // User info
    private Long mentorProfileId;
    private Long userId;
    private String email;
    private String fullName;
    private String phone;
    private LocalDate dob;
    private String gender;
    private String avatarUrl;
    private String status;
    
    // Mentor profile
    private String employeeCode;
    private String title;
    private String position;
    private String degree;
    private String specialization;
    private String researchInterests;
    private String department;
    private String faculty;
    private String officeLocation;
    private String officePhone;
    private String officeEmail;
    private String websiteUrl;
    private String officeHours;
    
    private Integer maxCoursesPerSemester;
    private Integer maxTeamsToMentor;
    private Integer currentCourseCount;
    private Integer currentTeamCount;
    
    private BigDecimal averageRating;
    private Integer totalReviews;
    
    private String bio;
    private String educationBackground;
    private String workExperience;
    
    private Boolean isAvailable;
    private Boolean isActive;
    
    private LocalDate joinedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

