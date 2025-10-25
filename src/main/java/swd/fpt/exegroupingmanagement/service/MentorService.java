package swd.fpt.exegroupingmanagement.service;

import swd.fpt.exegroupingmanagement.dto.request.CreateMentorRequest;
import swd.fpt.exegroupingmanagement.dto.request.UpdateMentorRequest;
import swd.fpt.exegroupingmanagement.dto.response.MentorDetailResponse;
import swd.fpt.exegroupingmanagement.dto.response.MentorResponse;

import java.util.List;

public interface MentorService {
    
    MentorDetailResponse createMentor(CreateMentorRequest request);
    
    MentorDetailResponse updateMentor(Long userId, UpdateMentorRequest request);
    
    MentorDetailResponse getMentorByUserId(Long userId);
    
    MentorDetailResponse getMentorByEmployeeCode(String employeeCode);
    
    List<MentorResponse> getAllMentors();
    
    List<MentorResponse> getAvailableMentors();
    
    List<MentorResponse> getMentorsByDepartment(String department);
    
    void deleteMentor(Long userId);
    
    void toggleMentorAvailability(Long userId, Boolean isAvailable);
}

