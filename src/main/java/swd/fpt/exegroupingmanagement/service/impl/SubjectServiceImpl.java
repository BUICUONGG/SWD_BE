package swd.fpt.exegroupingmanagement.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.SubjectRequest;
import swd.fpt.exegroupingmanagement.dto.response.SubjectResponse;
import swd.fpt.exegroupingmanagement.entity.SubjectEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.mapper.SubjectMapper;
import swd.fpt.exegroupingmanagement.repository.SubjectRepository;
import swd.fpt.exegroupingmanagement.service.SubjectService;
import swd.fpt.exegroupingmanagement.specification.SubjectSpecification;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    SubjectMapper subjectMapper;

    @Override
    @Transactional
    public SubjectResponse create(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            throw new ResourceConflictException("Mã môn học đã tồn tại");
        }
        SubjectEntity entity = subjectMapper.toEntity(request);
        SubjectEntity saved = subjectRepository.save(entity);
        return subjectMapper.toResponse(saved);
    }

    @Override
    public SubjectResponse getById(Long id) {
        SubjectEntity entity = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học"));
        return subjectMapper.toResponse(entity);
    }

    @Override
    public SubjectResponse getByCode(String code) {
        SubjectEntity entity = subjectRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học"));
        return subjectMapper.toResponse(entity);
    }

    @Override
    public List<SubjectResponse> getAll() {
        List<SubjectEntity> entities = subjectRepository.findAll();
        return subjectMapper.toResponseList(entities);
    }

    @Override
    @Transactional
    public SubjectResponse update(Long id, SubjectRequest request) {
        SubjectEntity entity = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học"));
        
        if (!entity.getCode().equals(request.getCode()) && 
            subjectRepository.existsByCode(request.getCode())) {
            throw new ResourceConflictException("Mã môn học đã tồn tại");
        }
        
        subjectMapper.updateEntity(entity, request);
        SubjectEntity updated = subjectRepository.save(entity);
        return subjectMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SubjectEntity entity = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học"));
        entity.setDeleted(true);
        subjectRepository.save(entity);
    }

    @Override
    public List<SubjectResponse> searchSubjects(String keyword) {
        Specification<SubjectEntity> spec = SubjectSpecification.hasKeyword(keyword);
        List<SubjectEntity> entities = subjectRepository.findAll(spec);
        return subjectMapper.toResponseList(entities);
    }
}

