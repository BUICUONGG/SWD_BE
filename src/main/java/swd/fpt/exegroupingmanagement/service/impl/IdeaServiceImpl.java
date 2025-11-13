package swd.fpt.exegroupingmanagement.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.repository.EnrollmentRepository;
import swd.fpt.exegroupingmanagement.repository.IdeaRepository;
import swd.fpt.exegroupingmanagement.service.IdeaService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdeaServiceImpl implements IdeaService {

    private final IdeaRepository ideaRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public IdeaEntity createIdea(Long enrollmentId, String name, String description) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));

        IdeaEntity idea = IdeaEntity.builder()
                .name(name)
                .description(description)
                .enrollment(enrollment)
                .build();
        return ideaRepository.save(idea);
    }

    public List<IdeaEntity> getIdeasByEnrollment(Long enrollmentId) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));
        return ideaRepository.findByEnrollment(enrollment);
    }
}
