package swd.fpt.exegroupingmanagement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import swd.fpt.exegroupingmanagement.entity.ApplicationEntity;
import swd.fpt.exegroupingmanagement.service.ApplicationService;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Application Management", description = "APIs for managing applications")
public class ApplicationController {
    ApplicationService applicationService;

    // student gửi đơn join team
    @PostMapping("/apply")
    public ResponseEntity<?> applyToTeam(
            @RequestParam Long enrollmentId,
            @RequestParam Long teamId
    ) {
        ApplicationEntity app = applicationService.applyToTeam(enrollmentId, teamId);
        return ResponseEntity.ok(StandardResponse.success("Gửi đơn tham gia thành công", app));
    }

    // leader mời student khác vào team
    @PostMapping("/invite")
    public ResponseEntity<?> inviteToTeam(
            @RequestParam Long leaderEnrollmentId,
            @RequestParam Long targetEnrollmentId
    ) {
        ApplicationEntity app = applicationService.inviteToTeam(leaderEnrollmentId, targetEnrollmentId);
        return ResponseEntity.ok(StandardResponse.success("Gửi lời mời thành công", app));
    }

    // leader xử lý đơn (accept / reject)
    @PutMapping("/{applicationId}/handle")
    public ResponseEntity<?> handleApplication(
            @RequestParam Long leaderEnrollmentId,
            @PathVariable Long applicationId,
            @RequestParam boolean accepted
    ) {
        applicationService.handleApplication(leaderEnrollmentId, applicationId, accepted);
        return ResponseEntity.ok(StandardResponse.success("Xử lý đơn thành công"));
    }
}