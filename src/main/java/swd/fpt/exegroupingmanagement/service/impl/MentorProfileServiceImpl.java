package swd.fpt.exegroupingmanagement.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.MentorProfileRequest;
import swd.fpt.exegroupingmanagement.dto.response.MentorProfileResponse;
import swd.fpt.exegroupingmanagement.entity.MentorProfileEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.mapper.MentorProfileMapper;
import swd.fpt.exegroupingmanagement.repository.MentorProfileRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import swd.fpt.exegroupingmanagement.service.MentorProfileService;
import swd.fpt.exegroupingmanagement.specification.MentorProfileSpecification;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MentorProfileServiceImpl implements MentorProfileService {
    MentorProfileRepository mentorProfileRepository;
    UserRepository userRepository;
    MentorProfileMapper mentorProfileMapper;

    @Override
    @Transactional
    public MentorProfileResponse create(MentorProfileRequest request) {

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        
        MentorProfileEntity entity = mentorProfileMapper.toEntity(request);
        entity.setUser(user);
        
        MentorProfileEntity saved = mentorProfileRepository.save(entity);
        return mentorProfileMapper.toResponse(saved);
    }

    @Override
    public MentorProfileResponse getById(Long id) {
        MentorProfileEntity entity = mentorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ giảng viên"));
        return mentorProfileMapper.toResponse(entity);
    }

    @Override
    public MentorProfileResponse getByUserId(Long userId) {
        MentorProfileEntity entity = mentorProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ giảng viên"));
        return mentorProfileMapper.toResponse(entity);
    }


    @Override
    public List<MentorProfileResponse> getAll() {
        List<MentorProfileEntity> entities = mentorProfileRepository.findAll();
        return mentorProfileMapper.toResponseList(entities);
    }

    @Override
    @Transactional
    public MentorProfileResponse update(Long id, MentorProfileRequest request) {
        MentorProfileEntity entity = mentorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ giảng viên"));
        
        mentorProfileMapper.updateEntity(entity, request);
        
        if (request.getUserId() != null && !entity.getUser().getUserId().equals(request.getUserId())) {
            UserEntity user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
            entity.setUser(user);
        }
        
        MentorProfileEntity updated = mentorProfileRepository.save(entity);
        return mentorProfileMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MentorProfileEntity entity = mentorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ giảng viên"));
        entity.setDeleted(true);
        mentorProfileRepository.save(entity);
    }

    @Override
    public List<MentorProfileResponse> searchProfiles(String keyword) {
        Specification<MentorProfileEntity> spec = MentorProfileSpecification.hasKeyword(keyword);
        List<MentorProfileEntity> entities = mentorProfileRepository.findAll(spec);
        return mentorProfileMapper.toResponseList(entities);
    }
}

