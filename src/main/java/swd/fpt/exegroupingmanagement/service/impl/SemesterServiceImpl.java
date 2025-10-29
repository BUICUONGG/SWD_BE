package swd.fpt.exegroupingmanagement.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.SemesterRequest;
import swd.fpt.exegroupingmanagement.dto.response.SemesterResponse;
import swd.fpt.exegroupingmanagement.entity.SemesterEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.mapper.SemesterMapper;
import swd.fpt.exegroupingmanagement.repository.SemesterRepository;
import swd.fpt.exegroupingmanagement.service.SemesterService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SemesterServiceImpl implements SemesterService {
    SemesterRepository semesterRepository;
    SemesterMapper semesterMapper;

    @Override
    @Transactional
    public SemesterResponse create(SemesterRequest request) {
        if (semesterRepository.existsByCode(request.getCode())) {
            throw new ResourceConflictException("Mã kỳ học đã tồn tại");
        }
        SemesterEntity entity = semesterMapper.toEntity(request);
        SemesterEntity saved = semesterRepository.save(entity);
        return semesterMapper.toResponse(saved);
    }

    @Override
    public SemesterResponse getById(Long id) {
        SemesterEntity entity = semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỳ học"));
        return semesterMapper.toResponse(entity);
    }

    @Override
    public SemesterResponse getByCode(String code) {
        SemesterEntity entity = semesterRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỳ học"));
        return semesterMapper.toResponse(entity);
    }

    @Override
    public List<SemesterResponse> getAll() {
        List<SemesterEntity> entities = semesterRepository.findAll();
        return semesterMapper.toResponseList(entities);
    }

    @Override
    @Transactional
    public SemesterResponse update(Long id, SemesterRequest request) {
        SemesterEntity entity = semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỳ học"));
        
        if (!entity.getCode().equals(request.getCode()) && 
            semesterRepository.existsByCode(request.getCode())) {
            throw new ResourceConflictException("Mã kỳ học đã tồn tại");
        }
        
        semesterMapper.updateEntity(entity, request);
        SemesterEntity updated = semesterRepository.save(entity);
        return semesterMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SemesterEntity entity = semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỳ học"));
        entity.setDeleted(true);
        semesterRepository.save(entity);
    }
}

