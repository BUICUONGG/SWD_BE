package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import swd.fpt.exegroupingmanagement.enums.ApplicationStatus;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "application", schema = "exegrouping")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Application extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id", nullable = false)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    @Builder.Default
    ApplicationStatus status = ApplicationStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    Enrollment enrollment;

    @ManyToOne
    @JoinColumn(name = "team_id")
    Team team;
}