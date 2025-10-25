package swd.fpt.exegroupingmanagement.service;

import java.util.List;

import swd.fpt.exegroupingmanagement.dto.request.EnrollmentRequest;
import swd.fpt.exegroupingmanagement.dto.response.EnrollmentResponse;

public interface EnrollmentService {
    EnrollmentResponse enroll(EnrollmentRequest request);
    EnrollmentResponse getById(Long id);
    List<EnrollmentResponse> getByUser(Long userId);
    List<EnrollmentResponse> getByCourse(Long courseId);
    EnrollmentResponse approveEnrollment(Long id, Long approvedBy);
    EnrollmentResponse completeEnrollment(Long id);
    void delete(Long id);
}

