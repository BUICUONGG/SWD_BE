package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.*;
import swd.fpt.exegroupingmanagement.enums.ApplicationStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    List<ApplicationEntity> findByTeamEntityAndStatus(TeamEntity team, ApplicationStatus status);

    List<ApplicationEntity> findByEnrollment(EnrollmentEntity enrollment);

    Optional<ApplicationEntity> findByTeamEntityAndEnrollment(TeamEntity team, EnrollmentEntity enrollment);

    boolean existsByTeamEntityAndEnrollmentAndStatusIn(
            TeamEntity team,
            EnrollmentEntity enrollment,
            List<ApplicationStatus> statuses
    );
}
