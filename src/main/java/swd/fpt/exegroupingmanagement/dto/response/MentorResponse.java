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
    private String title;
    private Integer currentCourseCount;
    private Integer maxCoursesPerSemester;
    private Boolean isAvailable;
}

