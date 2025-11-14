package swd.fpt.exegroupingmanagement.service;

import org.springframework.stereotype.Service;
import swd.fpt.exegroupingmanagement.dto.response.TeamResponse;

import java.util.List;

@Service
public interface TeamService {
    TeamResponse createTeam(Long enrollmentId, String teamName);
    TeamResponse getTeamDetails(Long teamId);
    TeamResponse selectMainIdea(Long teamId, Long leaderEnrollmentId, Long ideaId);
    List<TeamResponse> getMyTeams();
    List<TeamResponse> getTeamByEnrollment(Long enrollmentId);
    List<TeamResponse> getTeamsInCourse(Long courseId, Long mentorId);
    TeamResponse updateTeamName(Long teamId, Long leaderEnrollmentId, String teamName);
    void removeMember(Long teamId, Long leaderEnrollmentId, Long enrollmentId);
    void leaveTeam(Long teamId, Long enrollmentId);
    void disbandTeam(Long teamId, Long leaderEnrollmentId);
}
