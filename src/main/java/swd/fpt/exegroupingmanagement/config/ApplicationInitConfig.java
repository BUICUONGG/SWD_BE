package swd.fpt.exegroupingmanagement.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import swd.fpt.exegroupingmanagement.enums.Gender;
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

    private static final int TOTAL_STUDENTS = 30;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            log.info("🚀 Starting application initialization...");

            // Create data
            initializeData();

            log.info("✅ Application initialization completed!");
        };
    }

    private void initializeData() {
        try {
            // 1. Majors
            MajorEntity seMajor = createMajor("SE", "Software Engineering");

            // 2. Roles
            RoleEntity adminRole = createRole(PredefinedRole.ROLE_ADMIN);
            RoleEntity studentRole = createRole(PredefinedRole.ROLE_STUDENT);
            RoleEntity mentorRole = createRole(PredefinedRole.ROLE_MENTOR);

            // 3. Admin
            createAdmin(adminRole);

            // 4. Mentors
            List<UserEntity> mentors = createMentors(mentorRole);

            // 5. Students
            List<UserEntity> students = createStudents(studentRole, seMajor);

            // 6. Subject & Semester
            SubjectEntity subject = createSubject();
            SemesterEntity semester = createSemester();

            // 7. Course
            CourseEntity course = createCourse(subject, semester, mentors.get(0));

            // 8. Enrollments
            enrollStudents(students, course);

            log.info("📊 Summary: {} users, {} enrollments",
                    userRepository.count(), enrollmentRepository.count());
        } catch (Exception e) {
            log.error("❌ Error during initialization: {}", e.getMessage(), e);
        }
    }

    private MajorEntity createMajor(String code, String name) {
        return majorRepository.findByCode(code).orElseGet(() -> {
            MajorEntity major = MajorEntity.builder()
                    .code(code)
                    .name(name)
                    .isActive(true)
                    .build();
            return majorRepository.save(major);
        });
    }

    private RoleEntity createRole(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseGet(() -> {
            RoleEntity role = RoleEntity.builder()
                    .roleName(roleName)
                    .build();
            return roleRepository.save(role);
        });
    }

    private void createAdmin(RoleEntity adminRole) {
        if (userRepository.findByEmail("admin@fpt.edu.vn").isEmpty()) {
            UserEntity admin = UserEntity.builder()
                    .email("admin@fpt.edu.vn")
                    .fullName("System Admin")
                    .status(UserStatus.ACTIVE)
                    .dob(LocalDate.of(1990, 1, 1))
                    .gender(Gender.MALE)
                    .role(adminRole)
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .build();
            userRepository.save(admin);
            log.info("✅ Created admin");
        }
    }

    private List<UserEntity> createMentors(RoleEntity mentorRole) {
        List<UserEntity> mentors = new ArrayList<>();
        String[] names = {"Nguyen Van Mentor", "Tran Thi Huong", "Le Van Tuan"};
        String[] shortNames = {"MentorNV", "HuongTT", "TuanLV"};

        for (int i = 0; i < 3; i++) {
            final int index = i;
            String email = String.format("mentor%d@fpt.edu.vn", index + 1);
            final String mentorName = names[index];
            final String shortName = shortNames[index];

            UserEntity mentor = userRepository.findByEmail(email).orElseGet(() -> {
                UserEntity m = UserEntity.builder()
                        .email(email)
                        .fullName(mentorName)
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(1985, 3, 15))
                        .gender(Gender.MALE)
                        .role(mentorRole)
                        .passwordHash(passwordEncoder.encode("mentor123"))
                        .build();
                userRepository.save(m);

                MentorProfileEntity profile = MentorProfileEntity.builder()
                        .shortName(shortName)
                        .user(m)
                        .build();
                mentorProfileRepository.save(profile);
                return m;
            });
            mentors.add(mentor);
        }
        log.info("✅ Created {} mentors", mentors.size());
        return mentors;
    }

    private List<UserEntity> createStudents(RoleEntity studentRole, MajorEntity major) {
        List<UserEntity> students = new ArrayList<>();
        String[] firstNames = {"Nguyen", "Tran", "Le", "Pham", "Hoang", "Vo", "Dang", "Bui", "Do", "Ngo"};
        String[] lastNames = {"An", "Binh", "Cuong", "Dung", "Em", "Phuong", "Giang", "Hoa", "Khoa", "Linh"};

        for (int i = 1; i <= TOTAL_STUDENTS; i++) {
            final int index = i;
            String email = String.format("student%d@fpt.edu.vn", index);
            UserEntity student = userRepository.findByEmail(email).orElseGet(() -> {
                String fullName = firstNames[index % 10] + " Van " + lastNames[(index - 1) % 10];
                UserEntity s = UserEntity.builder()
                        .email(email)
                        .fullName(fullName)
                        .status(UserStatus.ACTIVE)
                        .dob(LocalDate.of(2000 + (index % 5), (index % 12) + 1, (index % 28) + 1))
                        .gender(index % 2 == 0 ? Gender.MALE : Gender.FEMALE)
                        .role(studentRole)
                        .major(major)
                        .passwordHash(passwordEncoder.encode("student123"))
                        .build();
                return userRepository.save(s);
            });
            students.add(student);
        }
        log.info("✅ Created {} students", students.size());
        return students;
    }

    private SubjectEntity createSubject() {
        return subjectRepository.findByCode("EXE201").orElseGet(() -> {
            SubjectEntity subject = SubjectEntity.builder()
                    .code("EXE201")
                    .name("Experiential Entrepreneurship 2")
                    .prerequisiteCodes("EXE101")
                    .build();
            return subjectRepository.save(subject);
        });
    }

    private SemesterEntity createSemester() {
        return semesterRepository.findByCode("FA2025").orElseGet(() -> {
            SemesterEntity semester = SemesterEntity.builder()
                    .code("FA2025")
                    .name("Fall 2025")
                    .year(2025)
                    .term(SemesterTerm.FALL)
                    .startDate(LocalDate.of(2025, 9, 1))
                    .endDate(LocalDate.of(2025, 12, 31))
                    .build();
            return semesterRepository.save(semester);
        });
    }

    private CourseEntity createCourse(SubjectEntity subject, SemesterEntity semester, UserEntity mentor) {
        return courseRepository.findByCode("EXE201_FA25_01").orElseGet(() -> {
            CourseEntity course = CourseEntity.builder()
                    .code("EXE201_FA25_01")
                    .name("EXE201 - Fall 2025 - Class 01")
                    .subject(subject)
                    .semester(semester)
                    .mentor(mentor)
                    .status(CourseStatus.OPEN)
                    .maxStudents(50)
                    .currentStudents(0)
                    .build();
            return courseRepository.save(course);
        });
    }

    private List<EnrollmentEntity> enrollStudents(List<UserEntity> students, CourseEntity course) {
        List<EnrollmentEntity> enrollments = new ArrayList<>();
        
        for (UserEntity student : students) {
            EnrollmentEntity enrollment = enrollmentRepository.findByUserAndCourse(student, course)
                    .orElseGet(() -> {
                        EnrollmentEntity e = EnrollmentEntity.builder()
                                .user(student)
                                .course(course)
                                .enrollmentDate(LocalDateTime.now().minusDays(5)) // Enrolled 5 days ago
                                .build();
                        return enrollmentRepository.save(e);
                    });
            enrollments.add(enrollment);
        }

        // Update course current students count
        course.setCurrentStudents(enrollments.size());
        courseRepository.save(course);

        log.info("✅ Enrolled and approved {} students", enrollments.size());
        return enrollments;
    }
}

