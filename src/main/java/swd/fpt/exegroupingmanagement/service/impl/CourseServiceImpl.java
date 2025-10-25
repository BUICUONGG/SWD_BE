package swd.fpt.exegroupingmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swd.fpt.exegroupingmanagement.dto.request.CreateCourseRequest;
import swd.fpt.exegroupingmanagement.dto.response.CourseResponse;
import swd.fpt.exegroupingmanagement.entity.CourseEntity;
import swd.fpt.exegroupingmanagement.entity.SemesterEntity;
import swd.fpt.exegroupingmanagement.entity.SubjectEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.repository.CourseRepository;
import swd.fpt.exegroupingmanagement.repository.SemesterRepository;
import swd.fpt.exegroupingmanagement.repository.SubjectRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import swd.fpt.exegroupingmanagement.service.CourseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request) {
        log.info("Creating course with code: {}", request.getCode());
        
        // Validate
        if (courseRepository.existsByCode(request.getCode())) {
            throw new ResourceConflictException("Course code already exists");
        }
        
        if (!subjectRepository.existsById(request.getSubjectId())) {
            throw new ResourceNotFoundException("Subject not found");
        }
        
        if (!semesterRepository.existsById(request.getSemesterId())) {
            throw new ResourceNotFoundException("Semester not found");
        }
        
        if (request.getMentorId() != null && !userRepository.existsById(request.getMentorId())) {
            throw new ResourceNotFoundException("Mentor not found");
        }
        
        // Create Course
        CourseEntity course = CourseEntity.builder()
            .subjectId(request.getSubjectId())
            .semesterId(request.getSemesterId())
            .mentorId(request.getMentorId())
            .code(request.getCode())
            .name(request.getName())
            .description(request.getDescription())
            .maxStudents(request.getMaxStudents())
            .minStudents(request.getMinStudents())
            .maxTeamSize(request.getMaxTeamSize())
            .minTeamSize(request.getMinTeamSize())
            .allowTeamFormation(request.getAllowTeamFormation() != null ? request.getAllowTeamFormation() : true)
            .teamFormationDeadline(request.getTeamFormationDeadline())
            .schedule(request.getSchedule())
            .room(request.getRoom())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .status(CourseStatus.DRAFT)
            .isActive(true)
            .build();
        
        CourseEntity savedCourse = courseRepository.save(course);
        
        log.info("Course created successfully: {}", savedCourse.getCourseId());
        
        return mapToResponse(savedCourse);
    }
    
    @Override
    public CourseResponse getCourseById(Long courseId) {
        CourseEntity course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        return mapToResponse(course);
    }
    
    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findByIsDeletedFalse().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseResponse> getCoursesByMentor(Long mentorId) {
        return courseRepository.findByMentorId(mentorId).stream()
            .filter(c -> !c.isDeleted())
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseResponse> getCoursesBySemester(Long semesterId) {
        return courseRepository.findBySemesterId(semesterId).stream()
            .filter(c -> !c.isDeleted())
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CourseResponse assignMentor(Long courseId, Long mentorId) {
        log.info("Assigning mentor {} to course {}", mentorId, courseId);
        
        CourseEntity course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        if (!userRepository.existsById(mentorId)) {
            throw new ResourceNotFoundException("Mentor not found");
        }
        
        course.setMentorId(mentorId);
        CourseEntity updated = courseRepository.save(course);
        
        log.info("Mentor assigned successfully");
        
        return mapToResponse(updated);
    }
    
    @Override
    @Transactional
    public void deleteCourse(Long courseId) {
        log.info("Deleting course: {}", courseId);
        
        CourseEntity course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        course.setDeleted(true);
        courseRepository.save(course);
        
        log.info("Course deleted successfully: {}", courseId);
    }
    
    // ===== MAPPING METHOD =====
    private CourseResponse mapToResponse(CourseEntity course) {
        SubjectEntity subject = subjectRepository.findById(course.getSubjectId()).orElse(null);
        SemesterEntity semester = semesterRepository.findById(course.getSemesterId()).orElse(null);
        UserEntity mentor = course.getMentorId() != null ? 
            userRepository.findById(course.getMentorId()).orElse(null) : null;
        
        return CourseResponse.builder()
            .courseId(course.getCourseId())
            .subjectId(course.getSubjectId())
            .subjectName(subject != null ? subject.getName() : null)
            .semesterId(course.getSemesterId())
            .semesterName(semester != null ? semester.getName() : null)
            .mentorId(course.getMentorId())
            .mentorName(mentor != null ? mentor.getFullName() : null)
            .code(course.getCode())
            .name(course.getName())
            .description(course.getDescription())
            .maxStudents(course.getMaxStudents())
            .currentStudents(course.getCurrentStudents())
            .minStudents(course.getMinStudents())
            .maxTeamSize(course.getMaxTeamSize())
            .minTeamSize(course.getMinTeamSize())
            .allowTeamFormation(course.getAllowTeamFormation())
            .teamFormationDeadline(course.getTeamFormationDeadline())
            .schedule(course.getSchedule())
            .room(course.getRoom())
            .startDate(course.getStartDate())
            .endDate(course.getEndDate())
            .status(course.getStatus())
            .isActive(course.getIsActive())
            .createdAt(course.getCreatedAt())
            .updatedAt(course.getUpdatedAt())
            .build();
    }
}

