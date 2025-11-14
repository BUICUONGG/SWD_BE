package swd.fpt.exegroupingmanagement.service;

import org.springframework.stereotype.Service;
import swd.fpt.exegroupingmanagement.entity.ApplicationEntity;

import java.util.List;

@Service
public interface ApplicationService {
    ApplicationEntity applyToTeam(Long enrollmentId, Long teamId);
    ApplicationEntity inviteToTeam(Long leaderEnrollmentId, Long targetEnrollmentId);
    void handleApplication(Long leaderEnrollmentId, Long applicationId, boolean accepted);
    List<ApplicationEntity> getMyApplications(Long enrollmentId);
    List<ApplicationEntity> getTeamApplications(Long teamId, Long leaderEnrollmentId);
    void cancelApplication(Long applicationId, Long enrollmentId);
}
