package swd.fpt.exegroupingmanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMentorRequest {
    
    // ===== USER INFO =====
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    private String phone;
    private LocalDate dob;
    private String gender;
    
    // ===== MENTOR PROFILE INFO =====
    @NotBlank(message = "Employee code is required")
    private String employeeCode;
    
    private String title;  // Dr., Prof.
    private String position;  // Lecturer, Professor
    private String degree;  // PhD, Master
    private String specialization;
    private String researchInterests;
    
    @NotBlank(message = "Department is required")
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
    
    private LocalDate joinedDate;
}

