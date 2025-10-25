package swd.fpt.exegroupingmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.dto.request.CreateMentorRequest;
import swd.fpt.exegroupingmanagement.dto.request.UpdateMentorRequest;
import swd.fpt.exegroupingmanagement.dto.response.MentorDetailResponse;
import swd.fpt.exegroupingmanagement.dto.response.MentorResponse;
import swd.fpt.exegroupingmanagement.entity.MentorProfileEntity;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.enums.Gender;
import swd.fpt.exegroupingmanagement.enums.UserStatus;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceConflictException;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.repository.MentorProfileRepository;
import swd.fpt.exegroupingmanagement.repository.RoleRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;
import swd.fpt.exegroupingmanagement.service.MentorService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorServiceImpl implements MentorService {
    
    private final UserRepository userRepository;
    private final MentorProfileRepository mentorProfileRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public MentorDetailResponse createMentor(CreateMentorRequest request) {
        log.info("Creating mentor with email: {}", request.getEmail());
        
        // Validate
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException("Email already exists");
        }
        if (mentorProfileRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ResourceConflictException("Employee code already exists");
        }
        
        // Get MENTOR role
        RoleEntity mentorRole = roleRepository.findById(PredefinedRole.ROLE_MENTOR)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor role not found"));
        
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(mentorRole);
        
        // Create User
        UserEntity user = UserEntity.builder()
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .fullName(request.getFullName())
            .dob(request.getDob())
            .gender(request.getGender() != null ? Gender.valueOf(request.getGender()) : Gender.MALE)
            .status(UserStatus.ACTIVE)
            .roles(roles)
            .build();
        
        UserEntity savedUser = userRepository.save(user);
        
        // Create Mentor Profile
        MentorProfileEntity profile = MentorProfileEntity.builder()
            .userId(savedUser.getUserId())
            .employeeCode(request.getEmployeeCode())
            .title(request.getTitle())
            .position(request.getPosition())
            .degree(request.getDegree())
            .specialization(request.getSpecialization())
            .researchInterests(request.getResearchInterests())
            .department(request.getDepartment())
            .faculty(request.getFaculty())
            .officeLocation(request.getOfficeLocation())
            .officePhone(request.getOfficePhone())
            .officeEmail(request.getOfficeEmail())
            .websiteUrl(request.getWebsiteUrl())
            .officeHours(request.getOfficeHours())
            .maxCoursesPerSemester(request.getMaxCoursesPerSemester())
            .maxTeamsToMentor(request.getMaxTeamsToMentor())
            .bio(request.getBio())
            .educationBackground(request.getEducationBackground())
            .workExperience(request.getWorkExperience())
            .joinedDate(request.getJoinedDate())
            .isAvailable(true)
            .build();
        
        MentorProfileEntity savedProfile = mentorProfileRepository.save(profile);
        
        log.info("Mentor created successfully: {}", savedUser.getUserId());
        
        return mapToDetailResponse(savedUser, savedProfile);
    }
    
    @Override
    @Transactional
    public MentorDetailResponse updateMentor(Long userId, UpdateMentorRequest request) {
        log.info("Updating mentor: {}", userId);
        
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));
        
        MentorProfileEntity profile = mentorProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor profile not found"));
        
        // Update User
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(user.getEmail()) && 
                userRepository.existsByEmail(request.getEmail())) {
                throw new ResourceConflictException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getDob() != null) user.setDob(request.getDob());
        if (request.getGender() != null) user.setGender(Gender.valueOf(request.getGender()));
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        
        userRepository.save(user);
        
        // Update Profile
        if (request.getTitle() != null) profile.setTitle(request.getTitle());
        if (request.getPosition() != null) profile.setPosition(request.getPosition());
        if (request.getDegree() != null) profile.setDegree(request.getDegree());
        if (request.getSpecialization() != null) profile.setSpecialization(request.getSpecialization());
        if (request.getResearchInterests() != null) profile.setResearchInterests(request.getResearchInterests());
        if (request.getDepartment() != null) profile.setDepartment(request.getDepartment());
        if (request.getFaculty() != null) profile.setFaculty(request.getFaculty());
        if (request.getOfficeLocation() != null) profile.setOfficeLocation(request.getOfficeLocation());
        if (request.getOfficePhone() != null) profile.setOfficePhone(request.getOfficePhone());
        if (request.getOfficeEmail() != null) profile.setOfficeEmail(request.getOfficeEmail());
        if (request.getWebsiteUrl() != null) profile.setWebsiteUrl(request.getWebsiteUrl());
        if (request.getOfficeHours() != null) profile.setOfficeHours(request.getOfficeHours());
        if (request.getMaxCoursesPerSemester() != null) 
            profile.setMaxCoursesPerSemester(request.getMaxCoursesPerSemester());
        if (request.getMaxTeamsToMentor() != null) 
            profile.setMaxTeamsToMentor(request.getMaxTeamsToMentor());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getEducationBackground() != null) 
            profile.setEducationBackground(request.getEducationBackground());
        if (request.getWorkExperience() != null) 
            profile.setWorkExperience(request.getWorkExperience());
        if (request.getIsAvailable() != null) profile.setIsAvailable(request.getIsAvailable());
        
        mentorProfileRepository.save(profile);
        
        log.info("Mentor updated successfully: {}", userId);
        
        return mapToDetailResponse(user, profile);
    }
    
    @Override
    public MentorDetailResponse getMentorByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));
        
        MentorProfileEntity profile = mentorProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor profile not found"));
        
        return mapToDetailResponse(user, profile);
    }
    
    @Override
    public MentorDetailResponse getMentorByEmployeeCode(String employeeCode) {
        MentorProfileEntity profile = mentorProfileRepository.findByEmployeeCode(employeeCode)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));
        
        UserEntity user = userRepository.findById(profile.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return mapToDetailResponse(user, profile);
    }
    
    @Override
    public List<MentorResponse> getAllMentors() {
        List<MentorProfileEntity> profiles = mentorProfileRepository.findByIsDeletedFalse();
        return profiles.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MentorResponse> getAvailableMentors() {
        List<MentorProfileEntity> profiles = mentorProfileRepository.findAvailableMentors();
        return profiles.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<MentorResponse> getMentorsByDepartment(String department) {
        List<MentorProfileEntity> profiles = mentorProfileRepository.findByDepartment(department);
        return profiles.stream()
            .filter(p -> !p.isDeleted())
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteMentor(Long userId) {
        log.info("Deleting mentor: {}", userId);
        
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));
        
        MentorProfileEntity profile = mentorProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor profile not found"));
        
        // Soft delete
        user.setDeleted(true);
        profile.setDeleted(true);
        
        userRepository.save(user);
        mentorProfileRepository.save(profile);
        
        log.info("Mentor deleted successfully: {}", userId);
    }
    
    @Override
    @Transactional
    public void toggleMentorAvailability(Long userId, Boolean isAvailable) {
        MentorProfileEntity profile = mentorProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor profile not found"));
        
        profile.setIsAvailable(isAvailable);
        mentorProfileRepository.save(profile);
        
        log.info("Mentor availability updated: {} - {}", userId, isAvailable);
    }
    
    // ===== MAPPING METHODS =====
    private MentorResponse mapToResponse(MentorProfileEntity profile) {
        UserEntity user = userRepository.findById(profile.getUserId())
            .orElse(null);
        
        if (user == null) return null;
        
        return MentorResponse.builder()
            .mentorProfileId(profile.getMentorProfileId())
            .userId(user.getUserId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .avatarUrl(user.getAvatarUrl())
            .employeeCode(profile.getEmployeeCode())
            .title(profile.getTitle())
            .position(profile.getPosition())
            .degree(profile.getDegree())
            .department(profile.getDepartment())
            .faculty(profile.getFaculty())
            .officeLocation(profile.getOfficeLocation())
            .officeHours(profile.getOfficeHours())
            .currentCourseCount(profile.getCurrentCourseCount())
            .currentTeamCount(profile.getCurrentTeamCount())
            .maxCoursesPerSemester(profile.getMaxCoursesPerSemester())
            .maxTeamsToMentor(profile.getMaxTeamsToMentor())
            .averageRating(profile.getAverageRating())
            .totalReviews(profile.getTotalReviews())
            .isAvailable(profile.getIsAvailable())
            .isActive(user.getStatus() == UserStatus.ACTIVE)
            .build();
    }
    
    private MentorDetailResponse mapToDetailResponse(UserEntity user, MentorProfileEntity profile) {
        return MentorDetailResponse.builder()
            .mentorProfileId(profile.getMentorProfileId())
            .userId(user.getUserId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .dob(user.getDob())
            .gender(user.getGender() != null ? user.getGender().name() : null)
            .avatarUrl(user.getAvatarUrl())
            .status(user.getStatus() != null ? user.getStatus().name() : null)
            .employeeCode(profile.getEmployeeCode())
            .title(profile.getTitle())
            .position(profile.getPosition())
            .degree(profile.getDegree())
            .specialization(profile.getSpecialization())
            .researchInterests(profile.getResearchInterests())
            .department(profile.getDepartment())
            .faculty(profile.getFaculty())
            .officeLocation(profile.getOfficeLocation())
            .officePhone(profile.getOfficePhone())
            .officeEmail(profile.getOfficeEmail())
            .websiteUrl(profile.getWebsiteUrl())
            .officeHours(profile.getOfficeHours())
            .maxCoursesPerSemester(profile.getMaxCoursesPerSemester())
            .maxTeamsToMentor(profile.getMaxTeamsToMentor())
            .currentCourseCount(profile.getCurrentCourseCount())
            .currentTeamCount(profile.getCurrentTeamCount())
            .averageRating(profile.getAverageRating())
            .totalReviews(profile.getTotalReviews())
            .bio(profile.getBio())
            .educationBackground(profile.getEducationBackground())
            .workExperience(profile.getWorkExperience())
            .isAvailable(profile.getIsAvailable())
            .isActive(user.getStatus() == UserStatus.ACTIVE)
            .joinedDate(profile.getJoinedDate())
            .createdAt(profile.getCreatedAt())
            .updatedAt(profile.getUpdatedAt())
            .build();
    }
}

