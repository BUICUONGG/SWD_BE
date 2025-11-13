package swd.fpt.exegroupingmanagement.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.request.CourseRequest;
import swd.fpt.exegroupingmanagement.dto.response.CourseResponse;
import swd.fpt.exegroupingmanagement.entity.CourseEntity;
import swd.fpt.exegroupingmanagement.entity.SemesterEntity;
import swd.fpt.exegroupingmanagement.entity.SubjectEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.mapper.CourseMapper;
import swd.fpt.exegroupingmanagement.repository.CourseRepository;
import swd.fpt.exegroupingmanagement.repository.SemesterRepository;
import swd.fpt.exegroupingmanagement.repository.SubjectRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import swd.fpt.exegroupingmanagement.service.CourseService;
import swd.fpt.exegroupingmanagement.specification.CourseSpecification;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseServiceImpl implements CourseService {
    CourseRepository courseRepository;
    SubjectRepository subjectRepository;
    SemesterRepository semesterRepository;
    UserRepository userRepository;
    CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseResponse create(CourseRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new ResourceConflictException("Mã lớp đã tồn tại");
        }
        
        CourseEntity entity = courseMapper.toEntity(request);
        
        // Set subject
        SubjectEntity subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học"));
        entity.setSubject(subject);
        
        // Set semester
        SemesterEntity semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỳ học"));
        entity.setSemester(semester);
        
        // Set mentor if provided
        if (request.getMentorId() != null) {
            UserEntity mentor = userRepository.findById(request.getMentorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));
            entity.setMentor(mentor);
        }
        
        // Set default values
        if (entity.getStatus() == null) {
            entity.setStatus(CourseStatus.UPCOMING);
        }
        if (entity.getCurrentStudents() == null) {
            entity.setCurrentStudents(0);
        }
        
        CourseEntity saved = courseRepository.save(entity);
        return courseMapper.toResponse(saved);
    }

    @Override
    public CourseResponse getById(Long id) {
        CourseEntity entity = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp"));
        return courseMapper.toResponse(entity);
    }

    @Override
    public CourseResponse getByCode(String code) {
        CourseEntity entity = courseRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp"));
        return courseMapper.toResponse(entity);
    }

    @Override
    public List<CourseResponse> getAll() {
        List<CourseEntity> entities = courseRepository.findAll();
        return courseMapper.toResponseList(entities);
    }

    @Override
    public List<CourseResponse> getByStatus(CourseStatus status) {
        List<CourseEntity> entities = courseRepository.findByStatus(status);
        return courseMapper.toResponseList(entities);
    }

    @Override
    public List<CourseResponse> getBySemester(Long semesterId) {
        List<CourseEntity> entities = courseRepository.findBySemester_SemesterId(semesterId);
        return courseMapper.toResponseList(entities);
    }

    @Override
    public List<CourseResponse> getByMentor(Long mentorId) {
        List<CourseEntity> entities = courseRepository.findByMentor_UserId(mentorId);
        return courseMapper.toResponseList(entities);
    }

    @Override
    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        CourseEntity entity = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp"));
        
        if (!entity.getCode().equals(request.getCode()) && 
            courseRepository.existsByCode(request.getCode())) {
            throw new ResourceConflictException("Mã lớp đã tồn tại");
        }
        
        courseMapper.updateEntity(entity, request);
        
        // Update subject
        SubjectEntity subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học"));
        entity.setSubject(subject);
        
        // Update semester
        SemesterEntity semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kỳ học"));
        entity.setSemester(semester);
        
        // Update mentor
        if (request.getMentorId() != null) {
            UserEntity mentor = userRepository.findById(request.getMentorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));
            entity.setMentor(mentor);
        } else {
            entity.setMentor(null);
        }
        
        CourseEntity updated = courseRepository.save(entity);
        return courseMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CourseEntity entity = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lớp"));
        entity.setDeleted(true);
        courseRepository.save(entity);
    }

    @Override
    public List<CourseResponse> searchCourses(String keyword, CourseStatus status, 
                                              Long semesterId, Long mentorId, Long subjectId) {
        Specification<CourseEntity> spec = Specification.where(null);
        
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(CourseSpecification.hasKeyword(keyword));
        }
        if (status != null) {
            spec = spec.and(CourseSpecification.hasStatus(status));
        }
        if (semesterId != null) {
            spec = spec.and(CourseSpecification.hasSemesterId(semesterId));
        }
        if (mentorId != null) {
            spec = spec.and(CourseSpecification.hasMentorId(mentorId));
        }
        if (subjectId != null) {
            spec = spec.and(CourseSpecification.hasSubjectId(subjectId));
        }
        
        List<CourseEntity> entities = courseRepository.findAll(spec);
        return courseMapper.toResponseList(entities);
    }
}
