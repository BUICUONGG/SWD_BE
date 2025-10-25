package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    Long enrollmentId;
  
    @Column(name = "enrollment_date", nullable = false)
    LocalDateTime enrollmentDate;
      
    @Column(name = "approved_by")
    Long approvedBy;
    
    @Column(name = "approved_at")
    LocalDateTime approvedAt;
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    @Nationalized
    String rejectionReason;
 
    @Column(name = "completed_at")
    LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    CourseEntity course;
}

