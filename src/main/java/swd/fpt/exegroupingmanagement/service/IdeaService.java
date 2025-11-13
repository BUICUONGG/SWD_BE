package swd.fpt.exegroupingmanagement.service;

import org.springframework.stereotype.Service;
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;

import java.util.List;

@Service
public interface IdeaService {
    IdeaEntity createIdea(Long enrollmentId, String name, String description);
    List<IdeaEntity> getIdeasByEnrollment(Long enrollmentId);
}
