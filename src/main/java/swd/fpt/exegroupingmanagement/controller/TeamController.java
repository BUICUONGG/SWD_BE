package swd.fpt.exegroupingmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import swd.fpt.exegroupingmanagement.entity.TeamEntity;
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
    @Operation(summary = "Create a new team",
            description = "Create a new team for a course enrollment. The creator becomes the team leader.")
    public ResponseEntity<StandardResponse<Object>> createTeam(
            @RequestParam Long enrollmentId,
            @RequestParam String teamName
    ) {
        TeamEntity team = teamService.createTeam(enrollmentId, teamName);
        return ResponseEntity.ok(success("Tạo team thành công", team));
    }

    // xem thông tin các team của user đã tham gia
    @GetMapping("/my-teams")
    @Operation(summary = "Get my teams",
            description = "Retrieve teams information for the given enrollment ID.")
    public ResponseEntity<StandardResponse<Object>> getMyTeam(@RequestParam Long enrollmentId) {
        return ResponseEntity.ok(success(teamService.getTeamByEnrollment(enrollmentId)));
    }

    // danh sách team thuộc một course
    @GetMapping("")
    @Operation(summary = "Get teams in a course",
            description = "Retrieve teams information for the given course ID.")
    public ResponseEntity<StandardResponse<Object>> getTeamsInCourse(@RequestParam Long CourseId,
                                                                     @RequestParam Long mentorId) {
        return ResponseEntity.ok(success(teamService.getTeamsInCourse(CourseId, mentorId)));
    }

    // leader chọn idea chính
    @PutMapping("/{teamId}/select-idea")
    @Operation(summary = "Select main idea for team",
            description = "Team leader selects the main idea for the team.")
    public ResponseEntity<StandardResponse<Object>> selectTeamIdea(
            @PathVariable Long teamId,
            @RequestParam Long ideaId
    ) {
        return ResponseEntity.ok(success(
                "Chọn idea chính thành công",
                teamService.selectMainIdea(teamId, ideaId)
        ));
    }
}
