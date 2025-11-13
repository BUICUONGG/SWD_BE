package swd.fpt.exegroupingmanagement.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
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
import swd.fpt.exegroupingmanagement.specification.MajorSpecification;

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
        entity.setDeleted(true);
        majorRepository.save(entity);
    }

    @Override
    public List<MajorResponse> searchMajors(String keyword) {
        Specification<MajorEntity> spec = MajorSpecification.hasKeyword(keyword);
        List<MajorEntity> entities = majorRepository.findAll(spec);
        return majorMapper.toResponseList(entities);
    }

    @Override
    public MajorEntity getMajorEntityByCode(String code) {
        return majorRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên ngành với mã: " + code));
    }

    @Override
    public String parseMajorCodeFromEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@fpt\\.edu\\.vn$")) {
            return null;
        }
        
        // Email format: vinhlqse182115@fpt.edu.vn
        // Extract major code (SE, SS, SA, etc.) from email
        String localPart = email.substring(0, email.indexOf("@"));
        
        // Pattern: nameXXnumber where XX is major code
        // Try to find 2 consecutive uppercase letters
        for (int i = 0; i < localPart.length() - 1; i++) {
            char c1 = Character.toUpperCase(localPart.charAt(i));
            char c2 = Character.toUpperCase(localPart.charAt(i + 1));
            
            if (Character.isLetter(c1) && Character.isLetter(c2)) {
                // Check if next characters are digits (to confirm this is the major code)
                if (i + 2 < localPart.length() && Character.isDigit(localPart.charAt(i + 2))) {
                    return String.valueOf(c1) + c2;
                }
            }
        }
        
        return null;
    }
}

