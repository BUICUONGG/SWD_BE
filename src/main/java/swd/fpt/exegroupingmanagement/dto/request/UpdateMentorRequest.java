package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMentorRequest {
    
    // ===== USER INFO =====
    private String fullName;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    private LocalDate dob;
    private String gender;
    private String avatarUrl;
    
    // ===== MENTOR PROFILE INFO =====
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
    private String bio;
    private String educationBackground;
    private String workExperience;
    private Boolean isAvailable;
}

