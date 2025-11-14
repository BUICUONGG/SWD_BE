package swd.fpt.exegroupingmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import swd.fpt.exegroupingmanagement.entity.ApplicationEntity;
import swd.fpt.exegroupingmanagement.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Application Management", description = "APIs for managing applications")
public class ApplicationController {
    ApplicationService applicationService;

    // student gửi đơn join team
    @PostMapping("/apply")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Apply to join a team", description = "Student sends application to join a team")
    public ResponseEntity<?> applyToTeam(
            @RequestParam Long enrollmentId,
            @RequestParam Long teamId
    ) {
        ApplicationEntity app = applicationService.applyToTeam(enrollmentId, teamId);
        return ResponseEntity.ok(StandardResponse.success("Gửi đơn tham gia thành công", app));
    }

    // leader mời student khác vào team
    @PostMapping("/invite")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Invite student to team", description = "Team leader invites a student to join the team")
    public ResponseEntity<?> inviteToTeam(
            @RequestParam Long leaderEnrollmentId,
            @RequestParam Long targetEnrollmentId
    ) {
        ApplicationEntity app = applicationService.inviteToTeam(leaderEnrollmentId, targetEnrollmentId);
        return ResponseEntity.ok(StandardResponse.success("Gửi lời mời thành công", app));
    }

    // leader xử lý đơn (accept / reject)
    @PutMapping("/{applicationId}/handle")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Handle application", description = "Team leader accepts or rejects an application")
    public ResponseEntity<?> handleApplication(
            @RequestParam Long leaderEnrollmentId,
            @PathVariable Long applicationId,
            @RequestParam boolean accepted
    ) {
        applicationService.handleApplication(leaderEnrollmentId, applicationId, accepted);
        return ResponseEntity.ok(StandardResponse.success("Xử lý đơn thành công"));
    }

    // Xem đơn của mình
    @GetMapping("/my-applications")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Get my applications", description = "Get all applications sent by or received by current student")
    public ResponseEntity<?> getMyApplications(@RequestParam Long enrollmentId) {
        List<ApplicationEntity> apps = applicationService.getMyApplications(enrollmentId);
        return ResponseEntity.ok(StandardResponse.success("Lấy danh sách đơn thành công", apps));
    }

    // Xem đơn gửi đến team (leader only)
    @GetMapping("/team/{teamId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Get team applications", description = "Team leader views all applications to their team")
    public ResponseEntity<?> getTeamApplications(
            @PathVariable Long teamId,
            @RequestParam Long leaderEnrollmentId
    ) {
        List<ApplicationEntity> apps = applicationService.getTeamApplications(teamId, leaderEnrollmentId);
        return ResponseEntity.ok(StandardResponse.success("Lấy danh sách đơn của team thành công", apps));
    }

    // Hủy đơn
    @DeleteMapping("/{applicationId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Cancel application", description = "Cancel a pending application")
    public ResponseEntity<?> cancelApplication(
            @PathVariable Long applicationId,
            @RequestParam Long enrollmentId
    ) {
        applicationService.cancelApplication(applicationId, enrollmentId);
        return ResponseEntity.ok(StandardResponse.success("Hủy đơn thành công"));
    }
}