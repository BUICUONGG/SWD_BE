//package swd.fpt.exegroupingmanagement.service.impl;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import swd.fpt.exegroupingmanagement.entity.*;
//import swd.fpt.exegroupingmanagement.enums.ApplicationStatus;
//import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
//import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
//import swd.fpt.exegroupingmanagement.repository.*;
//import swd.fpt.exegroupingmanagement.service.ApplicationService;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class ApplicationServiceImpl implements ApplicationService {
//
//    ApplicationRepository applicationRepository;
//    EnrollmentRepository enrollmentRepository;
//    TeamRepository teamRepository;
//    TeamMemberRepository teamMemberRepository;
//
//    @Transactional
//    public ApplicationEntity applyToTeam(Long enrollmentId, Long teamId) {
//        EnrollmentEntity enrollment = enrollmentRepository.findById(enrollmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Enrollment không tồn tại"));
//        TeamEntity team = teamRepository.findById(teamId)
//                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));
//
//        // check đã là member chưa
//        boolean isMember = teamMemberRepository.existsByTeamAndEnrollment(team, enrollment);
//        if (isMember) {
//            throw new ResourceConflictException("Bạn đã là thành viên của team này rồi");
//        }
//
//        // check đã gửi đơn pending hoặc accepted chưa
//        boolean alreadyApplied = applicationRepository.existsByTeamEntityAndEnrollmentAndStatusIn(
//                team, enrollment, List.of(ApplicationStatus.PENDING, ApplicationStatus.ACCEPTED)
//        );
//        if (alreadyApplied) {
//            throw new ResourceConflictException("Bạn đã gửi đơn vào team này rồi");
//        }
//
//        ApplicationEntity app = ApplicationEntity.builder()
//                .enrollment(enrollment)
//                .teamEntity(team)
//                .status(ApplicationStatus.PENDING)
//                .build();
//        return applicationRepository.save(app);
//    }
//
//    @Transactional
//    public ApplicationEntity inviteToTeam(Long leaderEnrollmentId, Long targetEnrollmentId) {
//        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Leader không tồn tại hoặc chưa có team"));
//        EnrollmentEntity target = enrollmentRepository.findById(targetEnrollmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Enrollment của người được mời không tồn tại"));
//
//        TeamEntity team = leader.getTeam();
//
//        // check nếu target đã có team khác
//        boolean alreadyInTeam = teamMemberRepository.existsByEnrollmentAndIsLeaderTrue(target)
//                || teamMemberRepository.existsByTeamAndEnrollment(team, target);
//        if (alreadyInTeam) {
//            throw new ResourceConflictException("Người này đã tham gia team khác");
//        }
//
//        // check nếu đã có lời mời trước đó
//        boolean alreadyInvited = applicationRepository.existsByTeamEntityAndEnrollmentAndStatusIn(
//                team, target, List.of(ApplicationStatus.PENDING)
//        );
//        if (alreadyInvited) {
//            throw new ResourceConflictException("Đã gửi lời mời đến người này rồi");
//        }
//
//        ApplicationEntity invite = ApplicationEntity.builder()
//                .enrollment(target)
//                .teamEntity(team)
//                .status(ApplicationStatus.PENDING)
//                .build();
//        return applicationRepository.save(invite);
//    }
//
//    @Transactional
//    public void handleApplication(Long leaderEnrollmentId, Long applicationId, boolean accepted) {
//        TeamMemberEntity leader = teamMemberRepository.findByEnrollment_EnrollmentIdAndIsLeaderTrue(leaderEnrollmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Bạn không phải leader hoặc chưa có team"));
//
//        ApplicationEntity app = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Đơn không tồn tại"));
//
//        if (!app.getTeamEntity().getId().equals(leader.getTeam().getId())) {
//            throw new ResourceConflictException("Không có quyền xử lý đơn của team khác");
//        }
//
//        if (app.getStatus() != ApplicationStatus.PENDING) {
//            throw new ResourceConflictException("Đơn đã được xử lý trước đó");
//        }
//
//        if (accepted) {
//            // Add member vào team
//            TeamMemberEntity newMember = TeamMemberEntity.builder()
//                    .team(app.getTeamEntity())
//                    .enrollment(app.getEnrollment())
//                    .isLeader(false)
//                    .build();
//            teamMemberRepository.save(newMember);
//            app.setStatus(ApplicationStatus.ACCEPTED);
//        } else {
//            app.setStatus(ApplicationStatus.REJECTED);
//        }
//
//        applicationRepository.save(app);
//    }
//}
