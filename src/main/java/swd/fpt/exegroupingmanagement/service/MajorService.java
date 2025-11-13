package swd.fpt.exegroupingmanagement.service;

import java.util.List;

import swd.fpt.exegroupingmanagement.dto.request.MajorRequest;
import swd.fpt.exegroupingmanagement.dto.response.MajorResponse;
import swd.fpt.exegroupingmanagement.entity.MajorEntity;

public interface MajorService {
    MajorResponse create(MajorRequest request);
    MajorResponse getById(Long id);
    MajorResponse getByCode(String code);
    MajorEntity getMajorEntityByCode(String code);
    List<MajorResponse> getAll();
    MajorResponse update(Long id, MajorRequest request);
    void delete(Long id);
    
    // Search method
    List<MajorResponse> searchMajors(String keyword);
    
    // Parse major code from FPT email
    String parseMajorCodeFromEmail(String email);
}

