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
public class MajorEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    Long majorId;
    
    @Column(name = "code", unique = true, nullable = false, length = 20)
    String code; // "SE" or "AI" or "IA"
    
    @Column(name = "name", nullable = false, length = 200)
    @Nationalized
    String name; // "Software Engineering" or "Artificial Intelligence" or "Intelligent Automation"
    
    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = true;
}

