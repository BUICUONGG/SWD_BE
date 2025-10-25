package swd.fpt.exegroupingmanagement.entity;

import java.time.LocalDate;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import swd.fpt.exegroupingmanagement.enums.SemesterTerm;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemesterEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    Long semesterId;
    
    @Column(name = "code", unique = true, nullable = false, length = 20)
    String code; // "FA2024"
    
    @Column(name = "name", nullable = false, length = 100)
    @Nationalized
    String name; // "Fall 2024"
    
    @Column(name = "year")
    Integer year; // 2024
    
    @Enumerated(EnumType.STRING)
    @Column(name = "term", length = 20)
    SemesterTerm term; // FALL
    
    @Column(name = "start_date", nullable = false)
    LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    LocalDate endDate;
    
}

