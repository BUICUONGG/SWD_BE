package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;
import swd.fpt.exegroupingmanagement.entity.TeamEntity;
import swd.fpt.exegroupingmanagement.entity.TeamMemberEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Long> {
    boolean existsByEnrollmentAndIsLeaderTrue(EnrollmentEntity enrollment);
    boolean existsByEnrollment(EnrollmentEntity enrollment);
    Optional<TeamMemberEntity> findByEnrollment_EnrollmentIdAndIsLeaderTrue(Long enrollmentId);
    Optional<TeamMemberEntity> findByTeamAndEnrollment(TeamEntity team, EnrollmentEntity enrollment);
    boolean existsByTeamAndEnrollment(TeamEntity team, EnrollmentEntity enrollment);
    List<TeamMemberEntity> findByTeam(TeamEntity team);
    List<TeamMemberEntity> findByEnrollment(EnrollmentEntity enrollment);
}
