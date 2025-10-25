package swd.fpt.exegroupingmanagement.service;

import java.util.List;

import swd.fpt.exegroupingmanagement.dto.request.MajorRequest;
import swd.fpt.exegroupingmanagement.dto.response.MajorResponse;

public interface MajorService {
    MajorResponse create(MajorRequest request);
    MajorResponse getById(Long id);
    MajorResponse getByCode(String code);
    List<MajorResponse> getAll();
    MajorResponse update(Long id, MajorRequest request);
    void delete(Long id);
}

