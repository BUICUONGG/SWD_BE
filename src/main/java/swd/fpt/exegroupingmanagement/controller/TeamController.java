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
import swd.fpt.exegroupingmanagement.service.TeamService;

import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Team Management", description = "APIs for managing teams")
public class TeamController {
    TeamService teamService;

    // tạo team mới
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Create a new team",
            description = "Create a new team for a course enrollment. The creator becomes the team leader.")
    public ResponseEntity<StandardResponse<Object>> createTeam(
            @RequestParam Long enrollmentId,
            @RequestParam String teamName
    ) {
        return ResponseEntity.ok(success("Tạo team thành công", teamService.createTeam(enrollmentId, teamName)));
    }

    // xem chi tiết team
    @GetMapping("/{teamId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get team details",
            description = "Get detailed information about a team including members and ideas.")
    public ResponseEntity<StandardResponse<Object>> getTeamDetails(@PathVariable Long teamId) {
        return ResponseEntity.ok(success("Lấy thông tin team thành công", teamService.getTeamDetails(teamId)));
    }

    // xem thông tin các team của user đã tham gia
    @GetMapping("/my-teams")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Get my teams",
            description = "Retrieve all teams of the current logged-in user.")
    public ResponseEntity<StandardResponse<Object>> getMyTeams() {
        return ResponseEntity.ok(success("Lấy danh sách team của bạn thành công", teamService.getMyTeams()));
    }

    // xem team theo enrollment ID cụ thể
    @GetMapping("/by-enrollment")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Get team by enrollment",
            description = "Retrieve teams information for the given enrollment ID.")
    public ResponseEntity<StandardResponse<Object>> getTeamByEnrollment(@RequestParam Long enrollmentId) {
        return ResponseEntity.ok(success(teamService.getTeamByEnrollment(enrollmentId)));
    }

    // danh sách team thuộc một course
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get teams in a course",
            description = "Retrieve teams information for the given course ID.")
    public ResponseEntity<StandardResponse<Object>> getTeamsInCourse(@RequestParam Long CourseId,
                                                                     @RequestParam Long mentorId) {
        return ResponseEntity.ok(success(teamService.getTeamsInCourse(CourseId, mentorId)));
    }

    // leader chọn idea chính
    @PutMapping("/{teamId}/select-idea")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Select main idea for team",
            description = "Team leader selects the main idea for the team.")
    public ResponseEntity<StandardResponse<Object>> selectTeamIdea(
            @PathVariable Long teamId,
            @RequestParam Long leaderEnrollmentId,
            @RequestParam Long ideaId
    ) {
        return ResponseEntity.ok(success(
                "Chọn idea chính thành công",
                teamService.selectMainIdea(teamId, leaderEnrollmentId, ideaId)
        ));
    }

    // cập nhật tên team
    @PutMapping("/{teamId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Update team name",
            description = "Team leader updates the team name.")
    public ResponseEntity<StandardResponse<Object>> updateTeamName(
            @PathVariable Long teamId,
            @RequestParam Long leaderEnrollmentId,
            @RequestParam String teamName
    ) {
        return ResponseEntity.ok(success(
                "Cập nhật tên team thành công",
                teamService.updateTeamName(teamId, leaderEnrollmentId, teamName)
        ));
    }

    // kick member khỏi team
    @DeleteMapping("/{teamId}/members/{enrollmentId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Remove team member",
            description = "Team leader removes a member from the team.")
    public ResponseEntity<StandardResponse<String>> removeMember(
            @PathVariable Long teamId,
            @PathVariable Long enrollmentId,
            @RequestParam Long leaderEnrollmentId
    ) {
        teamService.removeMember(teamId, leaderEnrollmentId, enrollmentId);
        return ResponseEntity.ok(success("Xóa thành viên thành công"));
    }

    // rời khỏi team
    @PostMapping("/{teamId}/leave")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Leave team",
            description = "Member leaves the team (not for leader).")
    public ResponseEntity<StandardResponse<String>> leaveTeam(
            @PathVariable Long teamId,
            @RequestParam Long enrollmentId
    ) {
        teamService.leaveTeam(teamId, enrollmentId);
        return ResponseEntity.ok(success("Rời khỏi team thành công"));
    }

    // giải tán team
    @DeleteMapping("/{teamId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Disband team",
            description = "Team leader disbands the team.")
    public ResponseEntity<StandardResponse<String>> disbandTeam(
            @PathVariable Long teamId,
            @RequestParam Long leaderEnrollmentId
    ) {
        teamService.disbandTeam(teamId, leaderEnrollmentId);
        return ResponseEntity.ok(success("Giải tán team thành công"));
    }
}
