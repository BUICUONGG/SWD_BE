package swd.fpt.exegroupingmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
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

    // xem list idea theo enrollment
    @GetMapping("/my-ideas")
    @Operation(summary = "Get my ideas",
            description = "Retrieve ideas for the given enrollment ID.")
    public ResponseEntity<?> getMyIdeas(@RequestParam Long enrollmentId) {
        List<IdeaEntity> ideas = ideaService.getIdeasByEnrollment(enrollmentId);
        return ResponseEntity.ok(StandardResponse.success(ideas));
    }
}