package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="subject")
public class SubjectEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    Long subjectId; 
    
    @Column(name = "code", unique = true, nullable = false, length = 20)
    String code;// "SWE201c"
    
    @Column(name = "name", nullable = false, length = 200)
    @Nationalized
    String name; // "Software Engineering"

    @Column(name = "prerequisite_codes", columnDefinition = "TEXT")
    String prerequisiteCodes; // "SWE201a,SWE201b"
   
}

