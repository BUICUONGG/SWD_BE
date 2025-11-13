package swd.fpt.exegroupingmanagement.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "enrollment")
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
    
 
    @Column(name = "completed_at")
    LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    CourseEntity course;

    @OneToMany
    @JoinColumn(name = "enrollment_id")
    List<ApplicationEntity> applications; // cac request tham gia nhom

    @OneToMany
    @JoinColumn(name = "enrollment_id")
    List<IdeaEntity> ideas;

    @OneToMany
    @JoinColumn(name = "enrollment_id")
    List<TeamMemberEntity> teamMembers; // cac nhom da tham gia
}

