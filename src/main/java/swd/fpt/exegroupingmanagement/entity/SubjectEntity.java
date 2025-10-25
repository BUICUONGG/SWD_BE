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
public class SubjectEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    Long subjectId; // "SWE201c"
    
    @Column(name = "code", unique = true, nullable = false, length = 20)
    String code;
    
    @Column(name = "name", nullable = false, length = 200)
    @Nationalized
    String name; // "Software Engineering"

    @Column(name = "prerequisite_ids", columnDefinition = "TEXT")
    String prerequisiteIds; // "SWE201a, SWE201b"
   
    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = true;
    
    // ===== RELATIONSHIPS =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    MajorEntity major;
}

