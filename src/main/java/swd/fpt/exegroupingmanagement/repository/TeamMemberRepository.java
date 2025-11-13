package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;
import swd.fpt.exegroupingmanagement.entity.TeamEntity;
import swd.fpt.exegroupingmanagement.entity.TeamMemberEntity;

import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Long> {
    boolean existsByEnrollmentAndIsLeaderTrue(EnrollmentEntity enrollment);
    Optional<TeamMemberEntity> findByEnrollment_EnrollmentIdAndIsLeaderTrue(Long enrollmentId);
    boolean existsByTeamAndEnrollment(TeamEntity team, EnrollmentEntity enrollment);
}
