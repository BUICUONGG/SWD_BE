package swd.fpt.exegroupingmanagement.service.impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swd.fpt.exegroupingmanagement.dto.response.TeamResponse;
import swd.fpt.exegroupingmanagement.entity.CourseEntity;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;
import swd.fpt.exegroupingmanagement.entity.TeamEntity;
import swd.fpt.exegroupingmanagement.entity.TeamMemberEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.mapper.TeamMapper;
import swd.fpt.exegroupingmanagement.repository.*;
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
    CourseRepository courseRepository;
    UserRepository userRepository;
    TeamMapper teamMapper;

    @Transactional
    public TeamResponse createTeam(String teamName) {
        // Get current logged-in user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Get all enrollments of current user
        List<EnrollmentEntity> enrollments = enrollmentRepository.findByUser_UserId(user.getUserId());
        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("Bạn chưa đăng ký khóa học nào");
        }

        // Clean up all orphan team members first (where team is deleted/null)
        for (EnrollmentEntity e : enrollments) {
            List<TeamMemberEntity> members = teamMemberRepository.findByEnrollment(e);
            for (TeamMemberEntity tm : members) {
                if (tm.getTeam() == null) {
                    teamMemberRepository.delete(tm);
                }
            }
        }

        // Find first enrollment without team
        EnrollmentEntity targetEnrollment = null;
        for (EnrollmentEntity e : enrollments) {
            List<TeamMemberEntity> members = teamMemberRepository.findByEnrollment(e);
            if (members.isEmpty()) {
                targetEnrollment = e;
                break;
            }
        }
        
        if (targetEnrollment == null) {
            throw new ResourceConflictException("Bạn đã có team trong tất cả các khóa học");
        }

        CourseEntity course = targetEnrollment.getCourse();

        // Tạo team mới
        TeamEntity team = TeamEntity.builder()
                .name(teamName)
                .course(course)
                .build();
        teamRepository.save(team);

        // Gán leader
        TeamMemberEntity leader = TeamMemberEntity.builder()
                .team(team)
                .enrollment(targetEnrollment)
                .isLeader(true)
                .build();
        teamMemberRepository.save(leader);

        return teamMapper.toResponse(team);
    }

    @Override
    public TeamResponse getTeamDetails(Long teamId) {
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));
        return teamMapper.toResponse(team);
    }

    @Transactional
    public TeamResponse selectMainIdea(Long teamId, Long leaderEnrollmentId, Long ideaId) {
        // Verify team exists
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));

        // Check leader
        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bạn không phải leader hoặc chưa tạo team"));

        if (!leader.getTeam().getId().equals(teamId)) {
            throw new ResourceConflictException("Bạn không phải leader của team này");
        }

        // check idea hợp lệ (phải thuộc 1 member trong team)
        IdeaEntity idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea không tồn tại"));

        boolean isInTeam = teamMemberRepository.existsByTeamAndEnrollment(team, idea.getEnrollment());
        if (!isInTeam) {
            throw new ResourceConflictException("Idea này không thuộc team của bạn");
        }

        // set idea chính
        team.setIdea(idea);
        teamRepository.save(team);
        return teamMapper.toResponse(team);
    }

    @Override
    public List<TeamResponse> getMyTeams() {
        // Get current logged-in user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // Get all enrollments of this user
        List<EnrollmentEntity> enrollments = enrollmentRepository.findByUser_UserId(user.getUserId());

        // Get all teams from TeamMember table directly
        List<TeamEntity> teams = enrollments.stream()
                .flatMap(enrollment -> teamMemberRepository.findByEnrollment(enrollment).stream())
                .map(TeamMemberEntity::getTeam)
                .filter(team -> team != null) // Filter out null teams
                .distinct()
                .toList();

        if (teams.isEmpty()) {
            throw new ResourceNotFoundException("Bạn chưa tham gia nhóm nào");
        }

        return teamMapper.toResponseList(teams);
    }

    @Override
    public List<TeamResponse> getTeamByEnrollment(Long enrollmentId) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));

        List<TeamEntity> teams = enrollment.getTeamMembers()
                .stream()
                .map(TeamMemberEntity::getTeam)
                .toList();

        return teamMapper.toResponseList(teams);
    }

    @Override
    public List<TeamResponse> getTeamsInCourse(Long courseId) {
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course không tồn tại"));
        
        List<TeamEntity> teams = teamRepository.findByCourse(course);

        return teamMapper.toResponseList(teams);
    }

    @Transactional
    @Override
    public TeamResponse updateTeamName(Long teamId, Long leaderEnrollmentId, String teamName) {
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));

        // Check leader
        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bạn không phải leader"));

        if (!leader.getTeam().getId().equals(teamId)) {
            throw new ResourceConflictException("Bạn không phải leader của team này");
        }

        team.setName(teamName);
        teamRepository.save(team);
        return teamMapper.toResponse(team);
    }

    @Transactional
    @Override
    public void removeMember(Long teamId, Long leaderEnrollmentId, Long enrollmentId) {
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));

        // Check leader
        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bạn không phải leader"));

        if (!leader.getTeam().getId().equals(teamId)) {
            throw new ResourceConflictException("Bạn không phải leader của team này");
        }

        // Không cho phép kick chính mình
        if (enrollmentId.equals(leaderEnrollmentId)) {
            throw new ResourceConflictException("Không thể tự kick chính mình, hãy dùng chức năng giải tán team");
        }

        // Find member to remove
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));

        TeamMemberEntity member = teamMemberRepository.findByTeamAndEnrollment(team, enrollment)
                .orElseThrow(() -> new ResourceNotFoundException("Thành viên không thuộc team này"));

        teamMemberRepository.delete(member);
    }

    @Transactional
    @Override
    public void leaveTeam(Long teamId, Long enrollmentId) {
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));

        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));

        TeamMemberEntity member = teamMemberRepository.findByTeamAndEnrollment(team, enrollment)
                .orElseThrow(() -> new ResourceNotFoundException("Bạn không thuộc team này"));

        // Leader không thể rời team, phải giải tán
        if (member.getIsLeader()) {
            throw new ResourceConflictException("Leader không thể rời team, hãy giải tán team hoặc chuyển quyền leader");
        }

        teamMemberRepository.delete(member);
    }

    @Transactional
    @Override
    public void disbandTeam(Long teamId, Long leaderEnrollmentId) {
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));

        // Check leader
        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bạn không phải leader"));

        if (!leader.getTeam().getId().equals(teamId)) {
            throw new ResourceConflictException("Bạn không phải leader của team này");
        }

        // Xóa tất cả members
        teamMemberRepository.deleteAll(team.getTeamMembers());

        // Xóa team
        teamRepository.delete(team);
    }
}
