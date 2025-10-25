package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Nationalized;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MentorProfileEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_profile_id")
    Long mentorProfileId;
    
    @Column(name = "employee_code", unique = true, length = 20)
    String employeeCode; // "GV001"
    
  
    @Column(name = "max_courses_per_semester")
    @Builder.Default
    Integer maxCoursesPerSemester = 3; // Tối đa dạy bao nhiêu lớp/kỳ: 3
    
    @Column(name = "current_course_count")
    @Builder.Default
    Integer currentCourseCount = 0;
    
    @Column(name = "current_team_count")
    @Builder.Default
    Integer currentTeamCount = 0; // Số lớp/nhóm đang dạy: 2
    
    @Column(name = "is_available")
    @Builder.Default
    Boolean isAvailable = true; // Có đang nhận lớp/nhóm mới không?

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    UserEntity user;
    
    public boolean canAcceptMoreCourses() {
        if (maxCoursesPerSemester == null) return true;
        return currentCourseCount < maxCoursesPerSemester;
    }

}

