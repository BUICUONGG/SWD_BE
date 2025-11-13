package swd.fpt.exegroupingmanagement.service;

import org.springframework.stereotype.Service;
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;
import swd.fpt.exegroupingmanagement.entity.TeamEntity;

import java.util.List;

@Service
public interface TeamService {
    TeamEntity createTeam(Long enrollmentId, String teamName);
    IdeaEntity selectMainIdea(Long leaderEnrollmentId, Long ideaId);

    List<TeamEntity> getTeamByEnrollment(Long enrollmentId);
}
