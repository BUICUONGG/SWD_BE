package swd.fpt.exegroupingmanagement.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;
import swd.fpt.exegroupingmanagement.entity.TeamEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.repository.EnrollmentRepository;
import swd.fpt.exegroupingmanagement.repository.IdeaRepository;
import swd.fpt.exegroupingmanagement.repository.TeamRepository;
import swd.fpt.exegroupingmanagement.service.IdeaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdeaServiceImpl implements IdeaService {

    IdeaRepository ideaRepository;
    EnrollmentRepository enrollmentRepository;
    TeamRepository teamRepository;

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

    @Override
    public IdeaEntity getIdeaById(Long ideaId) {
        return ideaRepository.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea không tồn tại"));
    }

    public List<IdeaEntity> getIdeasByEnrollment(Long enrollmentId) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));
        return ideaRepository.findByEnrollment(enrollment);
    }

    @Override
    public List<IdeaEntity> getIdeasByTeam(Long teamId) {
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));

        // Lấy tất cả ideas của các members trong team
        return team.getTeamMembers().stream()
                .flatMap(member -> ideaRepository.findByEnrollment(member.getEnrollment()).stream())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public IdeaEntity updateIdea(Long ideaId, Long enrollmentId, String name, String description) {
        IdeaEntity idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea không tồn tại"));

        // Check ownership
        if (!idea.getEnrollment().getEnrollmentId().equals(enrollmentId)) {
            throw new ResourceConflictException("Bạn không có quyền sửa idea này");
        }

        idea.setName(name);
        idea.setDescription(description);
        return ideaRepository.save(idea);
    }

    @Transactional
    @Override
    public void deleteIdea(Long ideaId, Long enrollmentId) {
        IdeaEntity idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea không tồn tại"));

        // Check ownership
        if (!idea.getEnrollment().getEnrollmentId().equals(enrollmentId)) {
            throw new ResourceConflictException("Bạn không có quyền xóa idea này");
        }

        // Check nếu idea đang được dùng làm main idea của team
        List<TeamEntity> teamsUsingIdea = teamRepository.findByIdea(idea);
        if (!teamsUsingIdea.isEmpty()) {
            throw new ResourceConflictException("Không thể xóa idea đang được dùng làm idea chính của team");
        }

        ideaRepository.delete(idea);
    }
}
