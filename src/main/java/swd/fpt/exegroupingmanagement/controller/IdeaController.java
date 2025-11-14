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
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;
import swd.fpt.exegroupingmanagement.service.IdeaService;

import java.util.List;

@RestController
@RequestMapping("/api/ideas")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Idea Management", description = "APIs for managing ideas")
public class IdeaController {
    IdeaService ideaService;

    // student tạo idea mới
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Create a new idea",
            description = "Create a new idea for a course enrollment.")
    public ResponseEntity<StandardResponse<Object>> createIdea(
            @RequestParam Long enrollmentId,
            @RequestParam String name,
            @RequestParam String description
    ) {
        IdeaEntity idea = ideaService.createIdea(enrollmentId, name, description);
        return ResponseEntity.ok(StandardResponse.success("Tạo idea thành công", idea));
    }

    // xem chi tiết idea
    @GetMapping("/{ideaId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get idea details",
            description = "Get detailed information about an idea.")
    public ResponseEntity<StandardResponse<Object>> getIdeaDetails(@PathVariable Long ideaId) {
        IdeaEntity idea = ideaService.getIdeaById(ideaId);
        return ResponseEntity.ok(StandardResponse.success("Lấy thông tin idea thành công", idea));
    }

    // xem list idea theo enrollment
    @GetMapping("/my-ideas")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Get my ideas",
            description = "Retrieve ideas for the given enrollment ID.")
    public ResponseEntity<?> getMyIdeas(@RequestParam Long enrollmentId) {
        List<IdeaEntity> ideas = ideaService.getIdeasByEnrollment(enrollmentId);
        return ResponseEntity.ok(StandardResponse.success(ideas));
    }

    // xem tất cả ideas của team
    @GetMapping("/team/{teamId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get team ideas",
            description = "Retrieve all ideas from team members.")
    public ResponseEntity<StandardResponse<Object>> getTeamIdeas(@PathVariable Long teamId) {
        List<IdeaEntity> ideas = ideaService.getIdeasByTeam(teamId);
        return ResponseEntity.ok(StandardResponse.success("Lấy danh sách idea của team thành công", ideas));
    }

    // cập nhật idea
    @PutMapping("/{ideaId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Update idea",
            description = "Update an idea (owner only).")
    public ResponseEntity<StandardResponse<Object>> updateIdea(
            @PathVariable Long ideaId,
            @RequestParam Long enrollmentId,
            @RequestParam String name,
            @RequestParam String description
    ) {
        IdeaEntity idea = ideaService.updateIdea(ideaId, enrollmentId, name, description);
        return ResponseEntity.ok(StandardResponse.success("Cập nhật idea thành công", idea));
    }

    // xóa idea
    @DeleteMapping("/{ideaId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    @Operation(summary = "Delete idea",
            description = "Delete an idea (owner only).")
    public ResponseEntity<StandardResponse<String>> deleteIdea(
            @PathVariable Long ideaId,
            @RequestParam Long enrollmentId
    ) {
        ideaService.deleteIdea(ideaId, enrollmentId);
        return ResponseEntity.ok(StandardResponse.success("Xóa idea thành công"));
    }
}