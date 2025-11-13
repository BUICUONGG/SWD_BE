package swd.fpt.exegroupingmanagement.service.impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;
import swd.fpt.exegroupingmanagement.entity.TeamEntity;
import swd.fpt.exegroupingmanagement.entity.TeamMemberEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.repository.EnrollmentRepository;
import swd.fpt.exegroupingmanagement.repository.IdeaRepository;
import swd.fpt.exegroupingmanagement.repository.TeamMemberRepository;
import swd.fpt.exegroupingmanagement.repository.TeamRepository;
import swd.fpt.exegroupingmanagement.service.TeamService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamServiceImpl implements TeamService {
    TeamRepository teamRepository;
    EnrollmentRepository enrollmentRepository;
    TeamMemberRepository teamMemberRepository;
    IdeaRepository ideaRepository;

    @Transactional
    public TeamEntity createTeam(Long enrollmentId, String teamName) {
        // Check enrollment tồn tại
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));

        // Check đã có team chưa (isLeader = true)
        boolean isAlreadyLeader = teamMemberRepository.existsByEnrollmentAndIsLeaderTrue(enrollment);
        if (isAlreadyLeader) {
            throw new ResourceConflictException("Bạn đã là leader của một team khác trong khóa học này");
        }

        // Tạo team mới
        TeamEntity team = TeamEntity.builder()
                .name(teamName)
                .course(enrollment.getCourse())
                .build();
        teamRepository.save(team);

        // Gán leader
        TeamMemberEntity leader = TeamMemberEntity.builder()
                .team(team)
                .enrollment(enrollment)
                .isLeader(true)
                .build();
        teamMemberRepository.save(leader);

        return team;
    }

    @Transactional
    public IdeaEntity selectMainIdea(Long leaderEnrollmentId, Long ideaId) {
        // check leader
        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Leader không tồn tại hoặc chưa tạo team"));

        TeamEntity team = leader.getTeam();

        // check idea hợp lệ (phải thuộc 1 member trong team)
        IdeaEntity idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea không tồn tại"));

        boolean isInTeam = teamMemberRepository.existsByTeamAndEnrollment(leader.getTeam(), idea.getEnrollment());
        if (!isInTeam) {
            throw new ResourceConflictException("Idea này không thuộc team của bạn");
        }

        // set idea chính
        team.setIdea(idea);
        teamRepository.save(team);
        return idea;
    }

    @Override
    public List<TeamEntity> getTeamByEnrollment(Long enrollmentId) {

        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));

        return enrollment.getTeamMembers()
                .stream()
                .map(TeamMemberEntity::getTeam)
                .toList();
    }
}
