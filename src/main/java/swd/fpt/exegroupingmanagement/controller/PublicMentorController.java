package swd.fpt.exegroupingmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swd.fpt.exegroupingmanagement.dto.response.MentorDetailResponse;
import swd.fpt.exegroupingmanagement.dto.response.MentorResponse;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import swd.fpt.exegroupingmanagement.service.MentorService;

import java.util.List;

@RestController
@RequestMapping("/api/public/mentors")
@RequiredArgsConstructor
@Tag(name = "Public - Mentor Info", description = "Public endpoints for viewing mentor information")
public class PublicMentorController {
    
    private final MentorService mentorService;
    
    @GetMapping
    @Operation(summary = "Get all mentors (Public)")
    public ResponseEntity<StandardResponse<List<MentorResponse>>> getAllMentors() {
        
        List<MentorResponse> response = mentorService.getAllMentors();
        
        return ResponseEntity.ok(StandardResponse.<List<MentorResponse>>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "Get mentor details (Public)")
    public ResponseEntity<StandardResponse<MentorDetailResponse>> getMentor(
            @PathVariable Long userId) {
        
        MentorDetailResponse response = mentorService.getMentorByUserId(userId);
        
        return ResponseEntity.ok(StandardResponse.<MentorDetailResponse>builder()
            .success(true)
            .data(response)
            .build());
    }
    
    @GetMapping("/employee/{employeeCode}")
    @Operation(summary = "Get mentor by employee code (Public)")
    public ResponseEntity<StandardResponse<MentorDetailResponse>> getMentorByCode(
            @PathVariable String employeeCode) {
        
        MentorDetailResponse response = mentorService.getMentorByEmployeeCode(employeeCode);
        
        return ResponseEntity.ok(StandardResponse.<MentorDetailResponse>builder()
            .success(true)
            .data(response)
            .build());
    }
}

