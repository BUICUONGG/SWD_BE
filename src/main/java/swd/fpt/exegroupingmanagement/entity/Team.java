package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import swd.fpt.exegroupingmanagement.enums.TeamStatus;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "team", schema = "exegrouping")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    @Lob
    @Column(name = "description")
    String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", length = 50)
    TeamStatus status = TeamStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "idea_id")
    Idea idea;
}