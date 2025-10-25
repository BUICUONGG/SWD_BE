package swd.fpt.exegroupingmanagement.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.MajorRequest;
import swd.fpt.exegroupingmanagement.dto.response.MajorResponse;
import swd.fpt.exegroupingmanagement.entity.MajorEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.mapper.MajorMapper;
import swd.fpt.exegroupingmanagement.repository.MajorRepository;
import swd.fpt.exegroupingmanagement.service.MajorService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MajorServiceImpl implements MajorService {
    MajorRepository majorRepository;
    MajorMapper majorMapper;

    @Override
    @Transactional
    public MajorResponse create(MajorRequest request) {
        if (majorRepository.existsByCode(request.getCode())) {
            throw new ResourceConflictException("Mã chuyên ngành đã tồn tại");
        }
        MajorEntity entity = majorMapper.toEntity(request);
        if (entity.getIsActive() == null) {
            entity.setIsActive(true);
        }
        MajorEntity saved = majorRepository.save(entity);
        return majorMapper.toResponse(saved);
    }

    @Override
    public MajorResponse getById(Long id) {
        MajorEntity entity = majorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên ngành"));
        return majorMapper.toResponse(entity);
    }

    @Override
    public MajorResponse getByCode(String code) {
        MajorEntity entity = majorRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên ngành"));
        return majorMapper.toResponse(entity);
    }

    @Override
    public List<MajorResponse> getAll() {
        List<MajorEntity> entities = majorRepository.findAll();
        return majorMapper.toResponseList(entities);
    }

    @Override
    @Transactional
    public MajorResponse update(Long id, MajorRequest request) {
        MajorEntity entity = majorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên ngành"));
        
        if (!entity.getCode().equals(request.getCode()) && 
            majorRepository.existsByCode(request.getCode())) {
            throw new ResourceConflictException("Mã chuyên ngành đã tồn tại");
        }
        
        majorMapper.updateEntity(entity, request);
        MajorEntity updated = majorRepository.save(entity);
        return majorMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MajorEntity entity = majorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên ngành"));
        majorRepository.delete(entity);
    }
}

