package swd.fpt.exegroupingmanagement.config;


import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.entity.*;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;
import swd.fpt.exegroupingmanagement.enums.SemesterTerm;
import swd.fpt.exegroupingmanagement.enums.UserStatus;
import swd.fpt.exegroupingmanagement.repository.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    UserRepository userRepository;
    RoleRepository roleRepository;
    MajorRepository majorRepository;
    SubjectRepository subjectRepository;
    SemesterRepository semesterRepository;
    CourseRepository courseRepository;
    EnrollmentRepository enrollmentRepository;
    MentorProfileRepository mentorProfileRepository;
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if (roleRepository.findByRoleName(PredefinedRole.ROLE_ADMIN).isEmpty()) {
                RoleEntity adminRole = RoleEntity.builder()
                        .roleName(PredefinedRole.ROLE_ADMIN)
                        .build();
                roleRepository.save(adminRole);
                UserEntity adminAccount = UserEntity.builder()
                            .email("admin@admin.com")
                        .fullName("Admin User")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2000, 1, 1))
                        .avatarUrl("https://vi.wikipedia.org/wiki/Cristiano_Ronaldo")
                        .role(adminRole)
                        .passwordHash(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(adminAccount);
            }

            if (roleRepository.findByRoleName(PredefinedRole.ROLE_STUDENT).isEmpty()) {

                RoleEntity studentRole = RoleEntity.builder()
                        .roleName(PredefinedRole.ROLE_STUDENT)
//                        .permissions(studentPermissions)
                        .build();
                roleRepository.save(studentRole);
                UserEntity studentAccount = UserEntity.builder()
                        .email("student@student.com")
                        .fullName("Student User")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2000, 1, 1))
                        .avatarUrl("https://example.com/student-avatar.png")
                        .role(studentRole)
                        .passwordHash(passwordEncoder.encode("student"))
                        .build();
                userRepository.save(studentAccount);
            }

            if (roleRepository.findByRoleName(PredefinedRole.ROLE_MENTOR).isEmpty()) {
                RoleEntity mentorRole = RoleEntity.builder()
                        .roleName(PredefinedRole.ROLE_MENTOR)
                        .build();
                roleRepository.save(mentorRole);
                UserEntity mentorAccount = UserEntity.builder()
                        .email("mentor@mentor.com")
                        .fullName("Nguyen Van Mentor")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(1985, 5, 15))
                        .avatarUrl("https://example.com/mentor-avatar.png")
                        .role(mentorRole)
                        .passwordHash(passwordEncoder.encode("mentor"))
                        .build();
                userRepository.save(mentorAccount);
            }
            
            // Initialize Majors
            log.info("Initializing majors...");
            if (majorRepository.count() == 0) {
                MajorEntity seMajor = MajorEntity.builder()
                        .code("SE")
                        .name("Software Engineering")
                        .isActive(true)
                        .build();
                majorRepository.save(seMajor);
                
                MajorEntity aiMajor = MajorEntity.builder()
                        .code("AI")
                        .name("Artificial Intelligence")
                        .isActive(true)
                        .build();
                majorRepository.save(aiMajor);
                
                MajorEntity iaMajor = MajorEntity.builder()
                        .code("IA")
                        .name("Information Assurance")
                        .isActive(true)
                        .build();
                majorRepository.save(iaMajor);
                
                log.info("Created 3 majors");
            }
            
            // Initialize Subjects
            log.info("Initializing subjects...");
            if (subjectRepository.count() == 0) {
                SubjectEntity sub1 = SubjectEntity.builder()
                        .code("SWE201c")
                        .name("Introduction to Software Engineering")
                        .prerequisiteCodes(null)
                        .build();
                subjectRepository.save(sub1);
                
                SubjectEntity sub2 = SubjectEntity.builder()
                        .code("SWE301c")
                        .name("Advanced Software Engineering")
                        .prerequisiteCodes("SWE201c")
                        .build();
                subjectRepository.save(sub2);
                
                SubjectEntity sub3 = SubjectEntity.builder()
                        .code("PRJ301")
                        .name("Java Web Application Development")
                        .prerequisiteCodes("SWE201c")
                        .build();
                subjectRepository.save(sub3);
                
                SubjectEntity sub4 = SubjectEntity.builder()
                        .code("SWD392")
                        .name("SW Architecture and Design")
                        .prerequisiteCodes("SWE301c,PRJ301")
                        .build();
                subjectRepository.save(sub4);
                
                SubjectEntity sub5 = SubjectEntity.builder()
                        .code("SWP391")
                        .name("Software development project")
                        .prerequisiteCodes("SWE301c,PRJ301")
                        .build();
                subjectRepository.save(sub5);
                
                log.info("Created 5 subjects");
            }
            
            // Initialize Semesters
            log.info("Initializing semesters...");
            if (semesterRepository.count() == 0) {
                SemesterEntity spring2024 = SemesterEntity.builder()
                        .code("SP2024")
                        .name("Spring 2024")
                        .year(2024)
                        .term(SemesterTerm.SPRING)
                        .startDate(LocalDate.of(2024, 1, 8))
                        .endDate(LocalDate.of(2024, 5, 31))
                        .build();
                semesterRepository.save(spring2024);
                
                SemesterEntity summer2024 = SemesterEntity.builder()
                        .code("SU2024")
                        .name("Summer 2024")
                        .year(2024)
                        .term(SemesterTerm.SUMMER)
                        .startDate(LocalDate.of(2024, 6, 1))
                        .endDate(LocalDate.of(2024, 8, 31))
                        .build();
                semesterRepository.save(summer2024);
                
                SemesterEntity fall2024 = SemesterEntity.builder()
                        .code("FA2024")
                        .name("Fall 2024")
                        .year(2024)
                        .term(SemesterTerm.FALL)
                        .startDate(LocalDate.of(2024, 9, 1))
                        .endDate(LocalDate.of(2024, 12, 31))
                        .build();
                semesterRepository.save(fall2024);
                
                log.info("Created 3 semesters");
            }
            
            // Initialize Mentor Profile
            log.info("Initializing mentor profiles...");
            if (mentorProfileRepository.count() == 0) {
                UserEntity mentor = userRepository.findByEmail("mentor@mentor.com")
                        .orElse(null);
                if (mentor != null) {
                    MentorProfileEntity profile = MentorProfileEntity.builder()
                            .employeeCode("GV001")
                            .user(mentor)
                            .maxCoursesPerSemester(3)
                            .build();
                    mentorProfileRepository.save(profile);
                    log.info("Created mentor profile for mentor@mentor.com");
                }
            }
            
            // Initialize Courses
            log.info("Initializing courses...");
            if (courseRepository.count() == 0) {
                SubjectEntity swp391 = subjectRepository.findByCode("SWP391").orElse(null);
                SubjectEntity swd392 = subjectRepository.findByCode("SWD392").orElse(null);
                SemesterEntity fall2024 = semesterRepository.findByCode("FA2024").orElse(null);
                UserEntity mentor = userRepository.findByEmail("mentor@mentor.com").orElse(null);
                
                if (swp391 != null && fall2024 != null) {
                    CourseEntity course1 = CourseEntity.builder()
                            .code("SWP391_FA24_01")
                            .name("Software development project - Class 01")
                            .maxStudents(30)
                            .currentStudents(0)
                            .teamFormationDeadline(LocalDateTime.of(2024, 9, 15, 23, 59))
                            .status(CourseStatus.OPEN)
                            .subject(swp391)
                            .semester(fall2024)
                            .mentor(mentor)
                            .build();
                    courseRepository.save(course1);
                }
                
                if (swd392 != null && fall2024 != null) {
                    CourseEntity course2 = CourseEntity.builder()
                            .code("SWD392_FA24_01")
                            .name("SW Architecture and Design - Class 01")
                            .maxStudents(35)
                            .currentStudents(0)
                            .teamFormationDeadline(LocalDateTime.of(2024, 9, 15, 23, 59))
                            .status(CourseStatus.OPEN)
                            .subject(swd392)
                            .semester(fall2024)
                            .mentor(mentor)
                            .build();
                    courseRepository.save(course2);
                }
                
                log.info("Created courses");
            }
            
            // Initialize Enrollments
            log.info("Initializing enrollments...");
            if (enrollmentRepository.count() == 0) {
                UserEntity student = userRepository.findByEmail("student@student.com").orElse(null);
                CourseEntity course = courseRepository.findByCode("SWP391_FA24_01").orElse(null);
                
                if (student != null && course != null) {
                    EnrollmentEntity enrollment = EnrollmentEntity.builder()
                            .user(student)
                            .course(course)
                            .enrollmentDate(LocalDateTime.now())
                            .build();
                    enrollmentRepository.save(enrollment);
                    
                    // Update course student count
                    course.setCurrentStudents(1);
                    courseRepository.save(course);
                    
                    log.info("Created enrollment for student in SWP391_FA24_01");
                }
            }
            
            log.info("Application initialization completed!");
        };
    }
}
