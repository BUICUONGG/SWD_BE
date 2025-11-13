package swd.fpt.exegroupingmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "team_member", schema = "exegrouping")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeamMemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    TeamEntity team;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    EnrollmentEntity enrollment;

    @Column(name = "is_leader", nullable = false)
    @Builder.Default
    Boolean isLeader = false;
}