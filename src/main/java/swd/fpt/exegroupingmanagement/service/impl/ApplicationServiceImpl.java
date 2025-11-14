package swd.fpt.exegroupingmanagement.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swd.fpt.exegroupingmanagement.entity.*;
import swd.fpt.exegroupingmanagement.enums.ApplicationStatus;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.repository.*;
import swd.fpt.exegroupingmanagement.service.ApplicationService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationServiceImpl implements ApplicationService {

    ApplicationRepository applicationRepository;
    EnrollmentRepository enrollmentRepository;
    TeamRepository teamRepository;
    TeamMemberRepository teamMemberRepository;

    @Transactional
    public ApplicationEntity applyToTeam(Long enrollmentId, Long teamId) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));

        // check đã là member của bất kỳ team nào chưa
        boolean isAlreadyInAnyTeam = teamMemberRepository.existsByEnrollment(enrollment);
        if (isAlreadyInAnyTeam) {
            throw new ResourceConflictException("Bạn đã tham gia một team khác rồi");
        }

        // check đã gửi đơn pending hoặc accepted chưa
        boolean alreadyApplied = applicationRepository.existsByTeamEntityAndEnrollmentAndStatusIn(
                team, enrollment, List.of(ApplicationStatus.APPLIED, ApplicationStatus.ACCEPTED)
        );
        if (alreadyApplied) {
            throw new ResourceConflictException("Bạn đã gửi đơn vào team này rồi");
        }

        ApplicationEntity app = ApplicationEntity.builder()
                .enrollment(enrollment)
                .teamEntity(team)
                .status(ApplicationStatus.APPLIED)
                .build();
        return applicationRepository.save(app);
    }

    @Transactional
    public ApplicationEntity inviteToTeam(Long leaderEnrollmentId, Long targetEnrollmentId) {
        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bạn không phải leader hoặc chưa có team"));
        EnrollmentEntity target = enrollmentRepository.findById(targetEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment của người được mời không tồn tại"));

        TeamEntity team = leader.getTeam();

        // check nếu target đã có team (là member của bất kỳ team nào)
        boolean alreadyInAnyTeam = teamMemberRepository.existsByEnrollment(target);
        if (alreadyInAnyTeam) {
            throw new ResourceConflictException("Người này đã tham gia team khác");
        }

        // check nếu đã có lời mời trước đó
        boolean alreadyInvited = applicationRepository.existsByTeamEntityAndEnrollmentAndStatusIn(
                team, target, List.of(ApplicationStatus.INVITED, ApplicationStatus.APPLIED)
        );
        if (alreadyInvited) {
            throw new ResourceConflictException("Đã có đơn liên quan đến người này rồi");
        }

        ApplicationEntity invite = ApplicationEntity.builder()
                .enrollment(target)
                .teamEntity(team)
                .status(ApplicationStatus.INVITED)
                .build();
        return applicationRepository.save(invite);
    }

    @Transactional
    public void handleApplication(Long leaderEnrollmentId, Long applicationId, boolean accepted) {
        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bạn không phải leader hoặc chưa có team"));

        ApplicationEntity app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Đơn không tồn tại"));

        if (!app.getTeamEntity().getId().equals(leader.getTeam().getId())) {
            throw new ResourceConflictException("Không có quyền xử lý đơn của team khác");
        }

        if (app.getStatus() != ApplicationStatus.APPLIED && app.getStatus() != ApplicationStatus.INVITED) {
            throw new ResourceConflictException("Đơn đã được xử lý trước đó");
        }

        if (accepted) {
            // Check xem người này đã vào team khác chưa
            boolean alreadyInAnyTeam = teamMemberRepository.existsByEnrollment(app.getEnrollment());
            if (alreadyInAnyTeam) {
                throw new ResourceConflictException("Người này đã tham gia team khác rồi");
            }

            // Add member vào team
            TeamMemberEntity newMember = TeamMemberEntity.builder()
                    .team(app.getTeamEntity())
                    .enrollment(app.getEnrollment())
                    .isLeader(false)
                    .build();
            teamMemberRepository.save(newMember);
            app.setStatus(ApplicationStatus.ACCEPTED);
        } else {
            app.setStatus(ApplicationStatus.REJECTED);
        }

        applicationRepository.save(app);
    }

    @Override
    public List<ApplicationEntity> getMyApplications(Long enrollmentId) {
        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));

        return applicationRepository.findByEnrollment(enrollment);
    }

    @Override
    public List<ApplicationEntity> getTeamApplications(Long teamId, Long leaderEnrollmentId) {
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));

        // Check leader
        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bạn không phải leader"));

        if (!leader.getTeam().getId().equals(teamId)) {
            throw new ResourceConflictException("Bạn không phải leader của team này");
        }

        return applicationRepository.findByTeamEntity(team);
    }

    @Transactional
    @Override
    public void cancelApplication(Long applicationId, Long enrollmentId) {
        ApplicationEntity app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Đơn không tồn tại"));

        // Check ownership
        if (!app.getEnrollment().getEnrollmentId().equals(enrollmentId)) {
            throw new ResourceConflictException("Bạn không có quyền hủy đơn này");
        }

        // Chỉ cho phép hủy đơn pending
        if (app.getStatus() != ApplicationStatus.APPLIED && app.getStatus() != ApplicationStatus.INVITED) {
            throw new ResourceConflictException("Chỉ có thể hủy đơn đang chờ xử lý");
        }

        applicationRepository.delete(app);
    }
}
