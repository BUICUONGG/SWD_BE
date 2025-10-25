package swd.fpt.exegroupingmanagement.service;

import java.util.List;

import swd.fpt.exegroupingmanagement.dto.request.MentorProfileRequest;
import swd.fpt.exegroupingmanagement.dto.response.MentorProfileResponse;

public interface MentorProfileService {
    MentorProfileResponse create(MentorProfileRequest request);
    MentorProfileResponse getById(Long id);
    MentorProfileResponse getByUserId(Long userId);
    MentorProfileResponse getByEmployeeCode(String employeeCode);
    List<MentorProfileResponse> getAll();
    MentorProfileResponse update(Long id, MentorProfileRequest request);
    void delete(Long id);
}

