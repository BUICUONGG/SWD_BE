package swd.fpt.exegroupingmanagement.service;

import org.springframework.stereotype.Service;
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;

import java.util.List;

@Service
public interface IdeaService {
    IdeaEntity createIdea(Long enrollmentId, String name, String description);
    IdeaEntity getIdeaById(Long ideaId);
    List<IdeaEntity> getIdeasByEnrollment(Long enrollmentId);
    List<IdeaEntity> getIdeasByTeam(Long teamId);
    IdeaEntity updateIdea(Long ideaId, Long enrollmentId, String name, String description);
    void deleteIdea(Long ideaId, Long enrollmentId);
}
