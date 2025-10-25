package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Nationalized;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    Long courseId;
    
    @Column(name = "code", unique = true, length = 20)
    String code;  // Mã lớp: "SE1234_FA2024"
    
    @Column(name = "name", nullable = false, length = 200)
    @Nationalized
    String name;  // Tên lớp: "Software Engineering - Class 01"

    @Column(name = "max_students")
    Integer maxStudents;
    
    @Column(name = "current_students")
    @Builder.Default
    Integer currentStudents = 0; 
    
    
    @Column(name = "team_formation_deadline")
    LocalDateTime teamFormationDeadline; // Deadline ghép nhóm

    @Column(name = "start_date")
    LocalDate startDate;
    
    @Column(name = "end_date")
    LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    CourseStatus status = CourseStatus.OPEN;
    
    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    UserEntity mentor;  
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    SubjectEntity subject;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    SemesterEntity semester;
}

