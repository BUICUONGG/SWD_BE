package swd.fpt.exegroupingmanagement.service;

import java.util.List;

import swd.fpt.exegroupingmanagement.dto.request.SemesterRequest;
import swd.fpt.exegroupingmanagement.dto.response.SemesterResponse;

public interface SemesterService {
    SemesterResponse create(SemesterRequest request);
    SemesterResponse getById(Long id);
    SemesterResponse getByCode(String code);
    List<SemesterResponse> getAll();
    SemesterResponse update(Long id, SemesterRequest request);
    void delete(Long id);
}

