package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import swd.fpt.exegroupingmanagement.enums.ApplicationStatus;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "application", schema = "exegrouping")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id", nullable = false)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    @Builder.Default
    ApplicationStatus status = ApplicationStatus.APPLIED;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    EnrollmentEntity enrollment;

    @ManyToOne
    @JoinColumn(name = "team_id")
    TeamEntity teamEntity;
}