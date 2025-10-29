package swd.fpt.exegroupingmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.EnrollmentRequest;
import swd.fpt.exegroupingmanagement.dto.response.EnrollmentResponse;
import swd.fpt.exegroupingmanagement.entity.CourseEntity;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.exception.exceptions.BusinessException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.mapper.EnrollmentMapper;
import swd.fpt.exegroupingmanagement.repository.CourseRepository;
import swd.fpt.exegroupingmanagement.repository.EnrollmentRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import swd.fpt.exegroupingmanagement.service.EnrollmentService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentServiceImpl implements EnrollmentService {
    EnrollmentRepository enrollmentRepository;
    UserRepository userRepository;
    CourseRepository courseRepository;
    EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentResponse enroll(EnrollmentRequest request) {
        if (enrollmentRepository.existsByUser_UserIdAndCourse_CourseId(
                request.getUserId(), request.getCourseId())) {
            throw new ResourceConflictException("Sinh viên đã đăng ký lớp này");
        }
        
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));
        
        CourseEntity course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp"));
        
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            throw new BusinessException("Lớp đã đầy");
        }
        
        EnrollmentEntity entity = EnrollmentEntity.builder()
                .user(user)
                .course(course)
                .enrollmentDate(LocalDateTime.now())
                .build();
        
        course.setCurrentStudents(course.getCurrentStudents() + 1);
        courseRepository.save(course);
        
        EnrollmentEntity saved = enrollmentRepository.save(entity);
        return enrollmentMapper.toResponse(saved);
    }

    @Override
    public EnrollmentResponse getById(Long id) {
        EnrollmentEntity entity = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đăng ký"));
        return enrollmentMapper.toResponse(entity);
    }

    @Override
    public List<EnrollmentResponse> getByUser(Long userId) {
        List<EnrollmentEntity> entities = enrollmentRepository.findByUser_UserId(userId);
        return enrollmentMapper.toResponseList(entities);
    }

    @Override
    public List<EnrollmentResponse> getByCourse(Long courseId) {
        List<EnrollmentEntity> entities = enrollmentRepository.findByCourse_CourseId(courseId);
        return enrollmentMapper.toResponseList(entities);
    }

    @Override
    @Transactional
    public EnrollmentResponse approveEnrollment(Long id, Long approvedBy) {
        EnrollmentEntity entity = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đăng ký"));
        
        entity.setApprovedBy(approvedBy);
        entity.setApprovedAt(LocalDateTime.now());
        
        EnrollmentEntity updated = enrollmentRepository.save(entity);
        return enrollmentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public EnrollmentResponse completeEnrollment(Long id) {
        EnrollmentEntity entity = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đăng ký"));
        
        entity.setCompletedAt(LocalDateTime.now());
        
        EnrollmentEntity updated = enrollmentRepository.save(entity);
        return enrollmentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EnrollmentEntity entity = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đăng ký"));
        
        CourseEntity course = entity.getCourse();
        course.setCurrentStudents(Math.max(0, course.getCurrentStudents() - 1));
        courseRepository.save(course);
        
        entity.setDeleted(true);
        enrollmentRepository.save(entity);
    }

    @Override
    public List<EnrollmentResponse> searchEnrollments(Long userId, Long courseId) {
        if (userId != null && courseId != null) {
            // Search by both
            return getByUser(userId).stream()
                    .filter(e -> e.getCourseId().equals(courseId))
                    .toList();
        } else if (userId != null) {
            return getByUser(userId);
        } else if (courseId != null) {
            return getByCourse(courseId);
        }
        return List.of();
    }
}

