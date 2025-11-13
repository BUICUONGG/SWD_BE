package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import swd.fpt.exegroupingmanagement.enums.TeamStatus;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "teamEntity", schema = "exegrouping")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeamEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    CourseEntity course;

    @Lob
    @Column(name = "name")
    String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", length = 50)
    TeamStatus status = TeamStatus.OPENING;

    @OneToOne
    @JoinColumn(name = "idea_id")
    IdeaEntity idea;

    @OneToMany
    @JoinColumn(name = "team_id")
    List<ApplicationEntity> applications;

    @OneToMany
    @JoinColumn(name = "team_id")
    List<TeamMemberEntity> teamMembers;
}