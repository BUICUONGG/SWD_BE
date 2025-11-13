package swd.fpt.exegroupingmanagement.config;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.entity.CourseEntity;
import swd.fpt.exegroupingmanagement.entity.EnrollmentEntity;
import swd.fpt.exegroupingmanagement.entity.MajorEntity;
import swd.fpt.exegroupingmanagement.entity.MentorProfileEntity;
import swd.fpt.exegroupingmanagement.entity.RoleEntity;
import swd.fpt.exegroupingmanagement.entity.SemesterEntity;
import swd.fpt.exegroupingmanagement.entity.SubjectEntity;
import swd.fpt.exegroupingmanagement.entity.UserEntity;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;
import swd.fpt.exegroupingmanagement.enums.SemesterTerm;
import swd.fpt.exegroupingmanagement.enums.UserStatus;
import swd.fpt.exegroupingmanagement.repository.CourseRepository;
import swd.fpt.exegroupingmanagement.repository.EnrollmentRepository;
import swd.fpt.exegroupingmanagement.repository.MajorRepository;
import swd.fpt.exegroupingmanagement.repository.MentorProfileRepository;
import swd.fpt.exegroupingmanagement.repository.RoleRepository;
import swd.fpt.exegroupingmanagement.repository.SemesterRepository;
import swd.fpt.exegroupingmanagement.repository.SubjectRepository;
import swd.fpt.exegroupingmanagement.repository.UserRepository;

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
            // Tạo majors TRƯỚC để users có thể reference
            if (majorRepository.count() == 0) {
                MajorEntity seMajor = MajorEntity.builder()
                        .code("SE")
                        .name("Software Engineering")
                        .isActive(true)
                        .build();
                majorRepository.save(seMajor);
                
                MajorEntity ssMajor = MajorEntity.builder()
                        .code("SS")
                        .name("Business")
                        .isActive(true)
                        .build();
                majorRepository.save(ssMajor);
                
                MajorEntity saMajor = MajorEntity.builder()
                        .code("SA")
                        .name("Languages")
                        .isActive(true)
                        .build();
                majorRepository.save(saMajor);
                
            }
            
            // Lấy majors để gán cho các tài khoản mẫu
            MajorEntity seMajorRef = majorRepository.findByCode("SE")
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy chuyên ngành SE"));
            MajorEntity ssMajorRef = majorRepository.findByCode("SS")
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy chuyên ngành SS"));
            MajorEntity saMajorRef = majorRepository.findByCode("SA")
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy chuyên ngành SA"));

            // Tạo roles và users SAU khi đã có majors
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
                        .build();
                roleRepository.save(studentRole);
                UserEntity studentAccount = UserEntity.builder()
                        .email("student@student.com")
                        .fullName("Student User")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2000, 1, 1))
                        .avatarUrl("https://example.com/student-avatar.png")
                        .role(studentRole)
                        .major(seMajorRef)
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
            
            if (subjectRepository.count() == 0) {
                SubjectEntity sub1 = SubjectEntity.builder()
                        .code("EXE101")
                        .name("Experiential Entrepreneurship 1")
                        .prerequisiteCodes(null)
                        .build();
                subjectRepository.save(sub1);
                
                SubjectEntity sub2 = SubjectEntity.builder()
                        .code("EXE201")
                        .name("Experiential Entrepreneurship 2")
                        .prerequisiteCodes("EXE101")
                        .build();
                subjectRepository.save(sub2);
                
            }
            
            if (semesterRepository.count() == 0) {
                SemesterEntity spring2024 = SemesterEntity.builder()
                        .code("SP2024")
                        .name("Spring 2024")
                        .year(2024)
                        .term(SemesterTerm.SPRING)
                        .startDate(LocalDate.of(2024, 1, 8))
                        .endDate(LocalDate.of(2024, 5, 7))
                        .build();
                semesterRepository.save(spring2024);
                
                SemesterEntity summer2024 = SemesterEntity.builder()
                        .code("SU2024")
                        .name("Summer 2024")
                        .year(2024)
                        .term(SemesterTerm.SUMMER)
                        .startDate(LocalDate.of(2024, 5, 15))
                        .endDate(LocalDate.of(2024, 9, 14))
                        .build();
                semesterRepository.save(summer2024);
                
                SemesterEntity fall2024 = SemesterEntity.builder()
                        .code("FA2024")
                        .name("Fall 2024")
                        .year(2024)
                        .term(SemesterTerm.FALL)
                        .startDate(LocalDate.of(2024, 9, 22))
                        .endDate(LocalDate.of(2025, 1, 21))
                        .build();
                semesterRepository.save(fall2024);
                
                SemesterEntity spring2025 = SemesterEntity.builder()
                        .code("SP2025")
                        .name("Spring 2025")
                        .year(2025)
                        .term(SemesterTerm.SPRING)
                        .startDate(LocalDate.of(2025, 1, 29))
                        .endDate(LocalDate.of(2025, 5, 28))
                        .build();
                semesterRepository.save(spring2025);
                
                SemesterEntity summer2025 = SemesterEntity.builder()
                        .code("SU2025")
                        .name("Summer 2025")
                        .year(2025)
                        .term(SemesterTerm.SUMMER)
                        .startDate(LocalDate.of(2025, 6, 5))
                        .endDate(LocalDate.of(2025, 10, 4))
                        .build();
                semesterRepository.save(summer2025);
                
                SemesterEntity fall2025 = SemesterEntity.builder()
                        .code("FA2025")
                        .name("Fall 2025")
                        .year(2025)
                        .term(SemesterTerm.FALL)
                        .startDate(LocalDate.of(2025, 10, 12))
                        .endDate(LocalDate.of(2026, 2, 10))
                        .build();
                semesterRepository.save(fall2025);
                
                SemesterEntity spring2026 = SemesterEntity.builder()
                        .code("SP2026")
                        .name("Spring 2026")
                        .year(2026)
                        .term(SemesterTerm.SPRING)
                        .startDate(LocalDate.of(2026, 2, 18))
                        .endDate(LocalDate.of(2026, 6, 17))
                        .build();
                semesterRepository.save(spring2026);
                
                SemesterEntity summer2026 = SemesterEntity.builder()
                        .code("SU2026")
                        .name("Summer 2026")
                        .year(2026)
                        .term(SemesterTerm.SUMMER)
                        .startDate(LocalDate.of(2026, 6, 25))
                        .endDate(LocalDate.of(2026, 10, 24))
                        .build();
                semesterRepository.save(summer2026);
                
                SemesterEntity fall2026 = SemesterEntity.builder()
                        .code("FA2026")
                        .name("Fall 2026")
                        .year(2026)
                        .term(SemesterTerm.FALL)
                        .startDate(LocalDate.of(2026, 11, 1))
                        .endDate(LocalDate.of(2027, 2, 28))
                        .build();
                semesterRepository.save(fall2026);
                
            }
            
            RoleEntity mentorRole = roleRepository.findByRoleName(PredefinedRole.ROLE_MENTOR).orElse(null);

            if (mentorProfileRepository.count() == 0 && mentorRole != null) {
                UserEntity mentor1 = userRepository.findByEmail("mentor@mentor.com").orElse(null);
                if (mentor1 != null) {
                    MentorProfileEntity profile1 = MentorProfileEntity.builder()
                            .shortName("MentorNV")
                            .user(mentor1)
                            .build();
                    mentorProfileRepository.save(profile1);
                }
                
                // Mentor 2
                UserEntity mentor2 = UserEntity.builder()
                        .email("tran.van.b@fpt.edu.vn")
                        .fullName("Tran Van B")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(1988, 3, 20))
                        .avatarUrl("https://example.com/mentor2-avatar.png")
                        .role(mentorRole)
                        .passwordHash(passwordEncoder.encode("mentor123"))
                        .build();
                userRepository.save(mentor2);
                MentorProfileEntity profile2 = MentorProfileEntity.builder()
                        .shortName("BTran")
                        .user(mentor2)
                        .build();
                mentorProfileRepository.save(profile2);
                
                // Mentor 3
                UserEntity mentor3 = UserEntity.builder()
                        .email("le.thi.c@fpt.edu.vn")
                        .fullName("Le Thi C")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(1990, 7, 12))
                        .avatarUrl("https://example.com/mentor3-avatar.png")
                        .role(mentorRole)
                        .passwordHash(passwordEncoder.encode("mentor123"))
                        .build();
                userRepository.save(mentor3);
                MentorProfileEntity profile3 = MentorProfileEntity.builder()
                        .shortName("CLe")
                        .user(mentor3)
                        .build();
                mentorProfileRepository.save(profile3);
                
                // Mentor 4
                UserEntity mentor4 = UserEntity.builder()
                        .email("pham.van.d@fpt.edu.vn")
                        .fullName("Pham Van D")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(1987, 11, 5))
                        .avatarUrl("https://example.com/mentor4-avatar.png")
                        .role(mentorRole)
                        .passwordHash(passwordEncoder.encode("mentor123"))
                        .build();
                userRepository.save(mentor4);
                MentorProfileEntity profile4 = MentorProfileEntity.builder()
                        .shortName("DPham")
                        .user(mentor4)
                        .build();
                mentorProfileRepository.save(profile4);
                
                // Mentor 5
                UserEntity mentor5 = UserEntity.builder()
                        .email("hoang.thi.e@fpt.edu.vn")
                        .fullName("Hoang Thi E")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(1992, 4, 18))
                        .avatarUrl("https://example.com/mentor5-avatar.png")
                        .role(mentorRole)
                        .passwordHash(passwordEncoder.encode("mentor123"))
                        .build();
                userRepository.save(mentor5);
                MentorProfileEntity profile5 = MentorProfileEntity.builder()
                        .shortName("EHoang")
                        .user(mentor5)
                        .build();
                mentorProfileRepository.save(profile5);
                
                log.info("Created 5 mentor profiles");
            }
            
            if (courseRepository.count() == 0) {
                SubjectEntity exe101 = subjectRepository.findByCode("EXE101").orElse(null);
                SubjectEntity exe201 = subjectRepository.findByCode("EXE201").orElse(null);
                
                SemesterEntity fall2024 = semesterRepository.findByCode("FA2024").orElse(null);
                SemesterEntity spring2025 = semesterRepository.findByCode("SP2025").orElse(null);
                SemesterEntity summer2024 = semesterRepository.findByCode("SU2024").orElse(null);
                
                UserEntity mentor1 = userRepository.findByEmail("mentor@mentor.com").orElse(null);
                UserEntity mentor2 = userRepository.findByEmail("tran.van.b@fpt.edu.vn").orElse(null);
                UserEntity mentor3 = userRepository.findByEmail("le.thi.c@fpt.edu.vn").orElse(null);
                UserEntity mentor4 = userRepository.findByEmail("pham.van.d@fpt.edu.vn").orElse(null);
                UserEntity mentor5 = userRepository.findByEmail("hoang.thi.e@fpt.edu.vn").orElse(null);
                
                // Course 1: EXE101 - Fall 2024 - Class 01
                if (exe101 != null && fall2024 != null && mentor1 != null) {
                    LocalDateTime teamFormationDeadline = fall2024.getStartDate()
                            .plusWeeks(1)
                            .atTime(LocalTime.of(23, 59));
                    CourseEntity course1 = CourseEntity.builder()
                            .code("EXE101_FA24_01")
                            .name("Experiential Entrepreneurship 1 - Class 01")
                            .maxStudents(35)
                            .currentStudents(0)
                            .teamFormationDeadline(teamFormationDeadline)
                            .status(CourseStatus.OPEN)
                            .subject(exe101)
                            .semester(fall2024)
                            .mentor(mentor1)
                            .build();
                    courseRepository.save(course1);
                }
                
                // Course 2: EXE101 - Fall 2024 - Class 02
                if (exe101 != null && fall2024 != null && mentor2 != null) {
                    LocalDateTime teamFormationDeadline = fall2024.getStartDate()
                            .plusWeeks(1)
                            .atTime(LocalTime.of(23, 59));
                    CourseEntity course2 = CourseEntity.builder()
                            .code("EXE101_FA24_02")
                            .name("Experiential Entrepreneurship 1 - Class 02")
                            .maxStudents(35)
                            .currentStudents(0)
                            .teamFormationDeadline(teamFormationDeadline)
                            .status(CourseStatus.OPEN)
                            .subject(exe101)
                            .semester(fall2024)
                            .mentor(mentor2)
                            .build();
                    courseRepository.save(course2);
                }
                
                // Course 3: EXE201 - Fall 2024 - Class 01
                if (exe201 != null && fall2024 != null && mentor3 != null) {
                    LocalDateTime teamFormationDeadline = fall2024.getStartDate()
                            .plusWeeks(1)
                            .atTime(LocalTime.of(23, 59));
                    CourseEntity course3 = CourseEntity.builder()
                            .code("EXE201_FA24_01")
                            .name("Experiential Entrepreneurship 2 - Class 01")
                            .maxStudents(30)
                            .currentStudents(0)
                            .teamFormationDeadline(teamFormationDeadline)
                            .status(CourseStatus.OPEN)
                            .subject(exe201)
                            .semester(fall2024)
                            .mentor(mentor3)
                            .build();
                    courseRepository.save(course3);
                }
                
                // Course 4: EXE101 - Spring 2025 - Class 01
                if (exe101 != null && spring2025 != null && mentor4 != null) {
                    LocalDateTime teamFormationDeadline = spring2025.getStartDate()
                            .plusWeeks(1)
                            .atTime(LocalTime.of(23, 59));
                    CourseEntity course4 = CourseEntity.builder()
                            .code("EXE101_SP25_01")
                            .name("Experiential Entrepreneurship 1 - Class 01")
                            .maxStudents(40)
                            .currentStudents(0)
                            .teamFormationDeadline(teamFormationDeadline)
                            .status(CourseStatus.UPCOMING)
                            .subject(exe101)
                            .semester(spring2025)
                            .mentor(mentor4)
                            .build();
                    courseRepository.save(course4);
                }
                
                // Course 5: EXE201 - Spring 2025 - Class 01
                if (exe201 != null && spring2025 != null && mentor5 != null) {
                    LocalDateTime teamFormationDeadline = spring2025.getStartDate()
                            .plusWeeks(1)
                            .atTime(LocalTime.of(23, 59));
                    CourseEntity course5 = CourseEntity.builder()
                            .code("EXE201_SP25_01")
                            .name("Experiential Entrepreneurship 2 - Class 01")
                            .maxStudents(30)
                            .currentStudents(0)
                            .teamFormationDeadline(teamFormationDeadline)
                            .status(CourseStatus.UPCOMING)
                            .subject(exe201)
                            .semester(spring2025)
                            .mentor(mentor5)
                            .build();
                    courseRepository.save(course5);
                }
                
                log.info("Created 5 courses");
            }
            
            // Initialize Additional Students & Enrollments (5 records)
            log.info("Initializing student users and enrollments...");
            RoleEntity studentRole = roleRepository.findByRoleName(PredefinedRole.ROLE_STUDENT).orElse(null);
            
            if (enrollmentRepository.count() == 0 && studentRole != null) {
                CourseEntity course1 = courseRepository.findByCode("EXE101_FA24_01").orElse(null);
                CourseEntity course2 = courseRepository.findByCode("EXE101_FA24_02").orElse(null);
                CourseEntity course3 = courseRepository.findByCode("EXE201_FA24_01").orElse(null);
                
                // Student 1 (existing)
                UserEntity student1 = userRepository.findByEmail("student@student.com").orElse(null);
                if (student1 != null && course1 != null) {
                    EnrollmentEntity enrollment1 = EnrollmentEntity.builder()
                            .user(student1)
                            .course(course1)
                            .enrollmentDate(LocalDateTime.now())
                            .build();
                    enrollmentRepository.save(enrollment1);
                    course1.setCurrentStudents(course1.getCurrentStudents() + 1);
                    courseRepository.save(course1);
                }
                
                // Student 2
                UserEntity student2 = UserEntity.builder()
                        .email("nguyen.van.f@fpt.edu.vn")
                        .fullName("Nguyen Van F")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2003, 5, 10))
                        .avatarUrl("https://example.com/student2-avatar.png")
                        .role(studentRole)
                        .passwordHash(passwordEncoder.encode("student123"))
                        .build();
                userRepository.save(student2);
                if (course1 != null) {
                    EnrollmentEntity enrollment2 = EnrollmentEntity.builder()
                            .user(student2)
                            .course(course1)
                            .enrollmentDate(LocalDateTime.now())
                            .build();
                    enrollmentRepository.save(enrollment2);
                    course1.setCurrentStudents(course1.getCurrentStudents() + 1);
                    courseRepository.save(course1);
                }
                
                // Student 3
                UserEntity student3 = UserEntity.builder()
                        .email("tran.thi.g@fpt.edu.vn")
                        .fullName("Tran Thi G")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2003, 8, 22))
                        .avatarUrl("https://example.com/student3-avatar.png")
                        .role(studentRole)
                        .passwordHash(passwordEncoder.encode("student123"))
                        .build();
                userRepository.save(student3);
                if (course2 != null) {
                    EnrollmentEntity enrollment3 = EnrollmentEntity.builder()
                            .user(student3)
                            .course(course2)
                            .enrollmentDate(LocalDateTime.now())
                            .build();
                    enrollmentRepository.save(enrollment3);
                    course2.setCurrentStudents(course2.getCurrentStudents() + 1);
                    courseRepository.save(course2);
                }
                
                // Student 4
                UserEntity student4 = UserEntity.builder()
                        .email("le.van.h@fpt.edu.vn")
                        .fullName("Le Van H")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2004, 2, 14))
                        .avatarUrl("https://example.com/student4-avatar.png")
                        .role(studentRole)
                        .passwordHash(passwordEncoder.encode("student123"))
                        .build();
                userRepository.save(student4);
                if (course2 != null) {
                    EnrollmentEntity enrollment4 = EnrollmentEntity.builder()
                            .user(student4)
                            .course(course2)
                            .enrollmentDate(LocalDateTime.now())
                            .build();
                    enrollmentRepository.save(enrollment4);
                    course2.setCurrentStudents(course2.getCurrentStudents() + 1);
                    courseRepository.save(course2);
                }
                
                // Student 5
                UserEntity student5 = UserEntity.builder()
                        .email("pham.thi.i@fpt.edu.vn")
                        .fullName("Pham Thi I")
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2003, 11, 30))
                        .avatarUrl("https://example.com/student5-avatar.png")
                        .role(studentRole)
                        .passwordHash(passwordEncoder.encode("student123"))
                        .build();
                userRepository.save(student5);
                if (course3 != null) {
                    EnrollmentEntity enrollment5 = EnrollmentEntity.builder()
                            .user(student5)
                            .course(course3)
                            .enrollmentDate(LocalDateTime.now())
                            .build();
                    enrollmentRepository.save(enrollment5);
                    course3.setCurrentStudents(course3.getCurrentStudents() + 1);
                    courseRepository.save(course3);
                }
                
                log.info("Created 5 enrollments for students");
            }
            
            log.info("ApplicationEntity initialization completed!");
        };
    }
}
