package swd.fpt.exegroupingmanagement.service;

import java.util.List;

import swd.fpt.exegroupingmanagement.dto.request.SubjectRequest;
import swd.fpt.exegroupingmanagement.dto.response.SubjectResponse;

public interface SubjectService {
    SubjectResponse create(SubjectRequest request);
    SubjectResponse getById(Long id);
    SubjectResponse getByCode(String code);
    List<SubjectResponse> getAll();
    SubjectResponse update(Long id, SubjectRequest request);
    void delete(Long id);
}

