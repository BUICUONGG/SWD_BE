package swd.fpt.exegroupingmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import swd.fpt.exegroupingmanagement.dto.request.CreateMentorRequest;
import swd.fpt.exegroupingmanagement.dto.request.UpdateMentorRequest;
import swd.fpt.exegroupingmanagement.dto.response.MentorDetailResponse;
import swd.fpt.exegroupingmanagement.dto.response.MentorResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import swd.fpt.exegroupingmanagement.service.MentorService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/mentors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin - Mentor Management", description = "Admin endpoints for managing mentors")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMentorController {
    
    private final MentorService mentorService;
    
    @PostMapping
    @Operation(summary = "Create new mentor")
    public ResponseEntity<StandardResponse<MentorDetailResponse>> createMentor(
            @Valid @RequestBody CreateMentorRequest request) {
        
        log.info("Admin creating mentor: {}", request.getEmail());
        
        MentorDetailResponse response = mentorService.createMentor(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(StandardResponse.<MentorDetailResponse>builder()
                .success(true)
                .message("Mentor created successfully")
                .data(response)
                .build());
    }
    
    @PutMapping("/{userId}")
    @Operation(summary = "Update mentor")
    public ResponseEntity<StandardResponse<MentorDetailResponse>> updateMentor(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateMentorRequest request) {
        
        log.info("Admin updating mentor: {}", userId);
        
        MentorDetailResponse response = mentorService.updateMentor(userId, request);
        
        return ResponseEntity.ok(StandardResponse.<MentorDetailResponse>builder()
            .success(true)
            .message("Mentor updated successfully")
            .data(response)
            .build());
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "Get mentor details")
    public ResponseEntity<StandardResponse<MentorDetailResponse>> getMentor(
            @PathVariable Long userId) {
        
        MentorDetailResponse response = mentorService.getMentorByUserId(userId);
        
        return ResponseEntity.ok(StandardResponse.<MentorDetailResponse>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @GetMapping
    @Operation(summary = "Get all mentors")
    public ResponseEntity<StandardResponse<List<MentorResponse>>> getAllMentors() {
        
        List<MentorResponse> response = mentorService.getAllMentors();
        
        return ResponseEntity.ok(StandardResponse.<List<MentorResponse>>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available mentors")
    public ResponseEntity<StandardResponse<List<MentorResponse>>> getAvailableMentors() {
        
        List<MentorResponse> response = mentorService.getAvailableMentors();
        
        return ResponseEntity.ok(StandardResponse.<List<MentorResponse>>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @GetMapping("/department/{department}")
    @Operation(summary = "Get mentors by department")
    public ResponseEntity<StandardResponse<List<MentorResponse>>> getMentorsByDepartment(
            @PathVariable String department) {
        
        List<MentorResponse> response = mentorService.getMentorsByDepartment(department);
        
        return ResponseEntity.ok(StandardResponse.<List<MentorResponse>>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete mentor")
    public ResponseEntity<StandardResponse<Void>> deleteMentor(@PathVariable Long userId) {
        
        log.info("Admin deleting mentor: {}", userId);
        
        mentorService.deleteMentor(userId);
        
        return ResponseEntity.ok(StandardResponse.<Void>builder()
            .success(true)
            .message("Mentor deleted successfully")
            .build());
    }
    
    @PatchMapping("/{userId}/availability")
    @Operation(summary = "Toggle mentor availability")
    public ResponseEntity<StandardResponse<Void>> toggleAvailability(
            @PathVariable Long userId,
            @RequestParam Boolean isAvailable) {
        
        log.info("Admin toggling mentor availability: {} - {}", userId, isAvailable);
        
        mentorService.toggleMentorAvailability(userId, isAvailable);
        
        return ResponseEntity.ok(StandardResponse.<Void>builder()
            .success(true)
            .message("Mentor availability updated")
            .build());
    }
}

