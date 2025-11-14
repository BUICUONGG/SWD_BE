package swd.fpt.exegroupingmanagement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.constant.PredefinedRole;
import swd.fpt.exegroupingmanagement.dto.response.DashboardStatisticsResponse;
import swd.fpt.exegroupingmanagement.dto.response.MentorPerformanceResponse;
import swd.fpt.exegroupingmanagement.dto.response.StudentReportResponse;
import swd.fpt.exegroupingmanagement.entity.*;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;
import swd.fpt.exegroupingmanagement.exception.exceptions.ResourceNotFoundException;
import swd.fpt.exegroupingmanagement.repository.*;
import swd.fpt.exegroupingmanagement.service.ReportService;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl implements ReportService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    CourseRepository courseRepository;
    EnrollmentRepository enrollmentRepository;
    TeamRepository teamRepository;
    TeamMemberRepository teamMemberRepository;
    IdeaRepository ideaRepository;
    SemesterRepository semesterRepository;
    MajorRepository majorRepository;
    MentorProfileRepository mentorProfileRepository;

    @Override
    public DashboardStatisticsResponse getDashboardStatistics() {
        RoleEntity studentRole = roleRepository.findByRoleName(PredefinedRole.ROLE_STUDENT)
                .orElse(null);
        RoleEntity mentorRole = roleRepository.findByRoleName(PredefinedRole.ROLE_MENTOR)
                .orElse(null);

        long totalStudents = studentRole != null ? userRepository.countByRole(studentRole) : 0;
        long totalMentors = mentorRole != null ? userRepository.countByRole(mentorRole) : 0;

        // Get latest semester for current stats
        List<SemesterEntity> semesters = semesterRepository.findAll();
        DashboardStatisticsResponse.SemesterStatistics currentSemesterStats = null;

        if (!semesters.isEmpty()) {
            SemesterEntity latestSemester = semesters.get(semesters.size() - 1);
            List<CourseEntity> semesterCourses = courseRepository.findBySemester(latestSemester);

            long semesterEnrollments = semesterCourses.stream()
                    .mapToLong(course -> enrollmentRepository.findByCourse(course).size())
                    .sum();

            long semesterTeams = semesterCourses.stream()
                    .mapToLong(course -> teamRepository.findByCourse(course).size())
                    .sum();

            double avgTeamSize = semesterTeams > 0
                    ? (double) teamMemberRepository.count() / semesterTeams
                    : 0;

            currentSemesterStats = DashboardStatisticsResponse.SemesterStatistics.builder()
                    .semesterCode(latestSemester.getCode())
                    .semesterName(latestSemester.getName())
                    .coursesCount((long) semesterCourses.size())
                    .enrollmentsCount(semesterEnrollments)
                    .teamsCount(semesterTeams)
                    .averageTeamSize(Math.round(avgTeamSize * 100.0) / 100.0)
                    .build();
        }

        return DashboardStatisticsResponse.builder()
                .totalUsers(userRepository.count())
                .totalStudents(totalStudents)
                .totalMentors(totalMentors)
                .totalCourses(courseRepository.count())
                .totalEnrollments(enrollmentRepository.count())
                .totalTeams(teamRepository.count())
                .totalIdeas(ideaRepository.count())
                .activeCourses(courseRepository.countByStatus(CourseStatus.OPEN))
                .completedCourses(courseRepository.countByStatus(CourseStatus.COMPLETED))
                .currentSemesterStats(currentSemesterStats)
                .build();
    }

    @Override
    public StudentReportResponse getStudentReportBySemester(Long semesterId) {
        SemesterEntity semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));

        List<CourseEntity> courses = courseRepository.findBySemester(semester);

        long totalEnrolled = 0;
        long totalInTeams = 0;
        List<StudentReportResponse.CourseStudentSummary> courseDetails = new ArrayList<>();

        for (CourseEntity course : courses) {
            List<EnrollmentEntity> enrollments = enrollmentRepository.findByCourse(course);
            List<TeamEntity> teams = teamRepository.findByCourse(course);

            long studentsInTeams = teams.stream()
                    .mapToLong(team -> teamMemberRepository.findByTeam(team).size())
                    .sum();

            totalEnrolled += enrollments.size();
            totalInTeams += studentsInTeams;

            double teamFormationRate = enrollments.size() > 0
                    ? (studentsInTeams * 100.0 / enrollments.size())
                    : 0;

            courseDetails.add(StudentReportResponse.CourseStudentSummary.builder()
                    .courseId(course.getCourseId())
                    .courseCode(course.getCode())
                    .courseName(course.getName())
                    .mentorName(course.getMentor().getFullName())
                    .enrolledCount((long) enrollments.size())
                    .teamsCount((long) teams.size())
                    .studentsInTeams(studentsInTeams)
                    .teamFormationRate(Math.round(teamFormationRate * 100.0) / 100.0)
                    .build());
        }

        RoleEntity studentRole = roleRepository.findByRoleName(PredefinedRole.ROLE_STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student role not found"));
        long totalStudents = userRepository.countByRole(studentRole);

        return StudentReportResponse.builder()
                .reportType("BY_SEMESTER")
                .filterName(semester.getName())
                .filterValue(semesterId)
                .totalStudents(totalStudents)
                .enrolledStudents(totalEnrolled)
                .studentsInTeams(totalInTeams)
                .studentsWithoutTeams(totalEnrolled - totalInTeams)
                .courseDetails(courseDetails)
                .build();
    }

    @Override
    public StudentReportResponse getStudentReportByMentor(Long mentorId, Long semesterId) {
        UserEntity mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor không tồn tại"));

        List<CourseEntity> courses;
        if (semesterId != null) {
            SemesterEntity semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));
            courses = courseRepository.findByMentorAndSemester(mentor, semester);
        } else {
            courses = courseRepository.findByMentor(mentor);
        }

        long totalEnrolled = 0;
        long totalInTeams = 0;
        List<StudentReportResponse.CourseStudentSummary> courseDetails = new ArrayList<>();

        for (CourseEntity course : courses) {
            List<EnrollmentEntity> enrollments = enrollmentRepository.findByCourse(course);
            List<TeamEntity> teams = teamRepository.findByCourse(course);

            long studentsInTeams = teams.stream()
                    .mapToLong(team -> teamMemberRepository.findByTeam(team).size())
                    .sum();

            totalEnrolled += enrollments.size();
            totalInTeams += studentsInTeams;

            double teamFormationRate = enrollments.size() > 0
                    ? (studentsInTeams * 100.0 / enrollments.size())
                    : 0;

            courseDetails.add(StudentReportResponse.CourseStudentSummary.builder()
                    .courseId(course.getCourseId())
                    .courseCode(course.getCode())
                    .courseName(course.getName())
                    .mentorName(mentor.getFullName())
                    .enrolledCount((long) enrollments.size())
                    .teamsCount((long) teams.size())
                    .studentsInTeams(studentsInTeams)
                    .teamFormationRate(Math.round(teamFormationRate * 100.0) / 100.0)
                    .build());
        }

        RoleEntity studentRole = roleRepository.findByRoleName(PredefinedRole.ROLE_STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student role not found"));
        long totalStudents = userRepository.countByRole(studentRole);

        return StudentReportResponse.builder()
                .reportType("BY_MENTOR")
                .filterName(mentor.getFullName())
                .filterValue(mentorId)
                .totalStudents(totalStudents)
                .enrolledStudents(totalEnrolled)
                .studentsInTeams(totalInTeams)
                .studentsWithoutTeams(totalEnrolled - totalInTeams)
                .courseDetails(courseDetails)
                .build();
    }

    @Override
    public StudentReportResponse getStudentReportByMajor(Long majorId, Long semesterId) {
        MajorEntity major = majorRepository.findById(majorId)
                .orElseThrow(() -> new ResourceNotFoundException("Major không tồn tại"));

        List<UserEntity> studentsInMajor = userRepository.findByMajor(major);

        List<CourseEntity> courses;
        if (semesterId != null) {
            SemesterEntity semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));
            courses = courseRepository.findBySemester(semester);
        } else {
            courses = courseRepository.findAll();
        }

        long totalEnrolled = 0;
        long totalInTeams = 0;
        List<StudentReportResponse.CourseStudentSummary> courseDetails = new ArrayList<>();

        for (CourseEntity course : courses) {
            List<EnrollmentEntity> enrollments = enrollmentRepository.findByCourse(course).stream()
                    .filter(e -> studentsInMajor.contains(e.getUser()))
                    .collect(Collectors.toList());

            if (enrollments.isEmpty()) continue;

            List<TeamEntity> teams = teamRepository.findByCourse(course);

            long studentsInTeams = teams.stream()
                    .flatMap(team -> teamMemberRepository.findByTeam(team).stream())
                    .filter(tm -> studentsInMajor.contains(tm.getEnrollment().getUser()))
                    .count();

            totalEnrolled += enrollments.size();
            totalInTeams += studentsInTeams;

            double teamFormationRate = enrollments.size() > 0
                    ? (studentsInTeams * 100.0 / enrollments.size())
                    : 0;

            courseDetails.add(StudentReportResponse.CourseStudentSummary.builder()
                    .courseId(course.getCourseId())
                    .courseCode(course.getCode())
                    .courseName(course.getName())
                    .mentorName(course.getMentor().getFullName())
                    .enrolledCount((long) enrollments.size())
                    .teamsCount((long) teams.size())
                    .studentsInTeams(studentsInTeams)
                    .teamFormationRate(Math.round(teamFormationRate * 100.0) / 100.0)
                    .build());
        }

        return StudentReportResponse.builder()
                .reportType("BY_MAJOR")
                .filterName(major.getName())
                .filterValue(majorId)
                .totalStudents((long) studentsInMajor.size())
                .enrolledStudents(totalEnrolled)
                .studentsInTeams(totalInTeams)
                .studentsWithoutTeams(totalEnrolled - totalInTeams)
                .courseDetails(courseDetails)
                .build();
    }

    @Override
    public Map<String, Object> getCourseStatistics(Long semesterId) {
        List<CourseEntity> courses;
        if (semesterId != null) {
            SemesterEntity semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));
            courses = courseRepository.findBySemester(semester);
        } else {
            courses = courseRepository.findAll();
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCourses", courses.size());
        stats.put("openCourses", courses.stream().filter(c -> c.getStatus() == CourseStatus.OPEN).count());
        stats.put("completedCourses", courses.stream().filter(c -> c.getStatus() == CourseStatus.COMPLETED).count());
        stats.put("totalEnrollments", courses.stream()
                .mapToLong(c -> enrollmentRepository.findByCourse(c).size()).sum());
        stats.put("averageStudentsPerCourse", courses.isEmpty() ? 0 :
                courses.stream().mapToLong(c -> enrollmentRepository.findByCourse(c).size()).average().orElse(0));

        return stats;
    }

    @Override
    public Map<String, Object> getTeamStatistics(Long semesterId, Long courseId) {
        List<TeamEntity> teams;

        if (courseId != null) {
            CourseEntity course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course không tồn tại"));
            teams = teamRepository.findByCourse(course);
        } else if (semesterId != null) {
            SemesterEntity semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));
            List<CourseEntity> courses = courseRepository.findBySemester(semester);
            teams = courses.stream()
                    .flatMap(c -> teamRepository.findByCourse(c).stream())
                    .collect(Collectors.toList());
        } else {
            teams = teamRepository.findAll();
        }

        long totalMembers = teams.stream()
                .mapToLong(t -> teamMemberRepository.findByTeam(t).size())
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTeams", teams.size());
        stats.put("totalMembers", totalMembers);
        stats.put("averageTeamSize", teams.isEmpty() ? 0 : (double) totalMembers / teams.size());
        stats.put("teamsWithIdea", teams.stream().filter(t -> t.getIdea() != null).count());

        return stats;
    }

    @Override
    public Map<String, Object> getEnrollmentStatistics(Long semesterId, Long majorId) {
        List<EnrollmentEntity> enrollments;

        if (semesterId != null && majorId != null) {
            SemesterEntity semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));
            MajorEntity major = majorRepository.findById(majorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Major không tồn tại"));

            List<CourseEntity> courses = courseRepository.findBySemester(semester);
            List<UserEntity> studentsInMajor = userRepository.findByMajor(major);

            enrollments = courses.stream()
                    .flatMap(c -> enrollmentRepository.findByCourse(c).stream())
                    .filter(e -> studentsInMajor.contains(e.getUser()))
                    .collect(Collectors.toList());
        } else if (semesterId != null) {
            SemesterEntity semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));
            List<CourseEntity> courses = courseRepository.findBySemester(semester);
            enrollments = courses.stream()
                    .flatMap(c -> enrollmentRepository.findByCourse(c).stream())
                    .collect(Collectors.toList());
        } else if (majorId != null) {
            MajorEntity major = majorRepository.findById(majorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Major không tồn tại"));
            List<UserEntity> studentsInMajor = userRepository.findByMajor(major);
            enrollments = enrollmentRepository.findAll().stream()
                    .filter(e -> studentsInMajor.contains(e.getUser()))
                    .collect(Collectors.toList());
        } else {
            enrollments = enrollmentRepository.findAll();
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEnrollments", enrollments.size());
        stats.put("uniqueStudents", enrollments.stream().map(e -> e.getUser().getUserId()).distinct().count());
        stats.put("uniqueCourses", enrollments.stream().map(e -> e.getCourse().getCourseId()).distinct().count());

        return stats;
    }

    @Override
    public List<MentorPerformanceResponse> getMentorPerformanceReport(Long semesterId) {
        RoleEntity mentorRole = roleRepository.findByRoleName(PredefinedRole.ROLE_MENTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor role not found"));

        List<UserEntity> mentors = userRepository.findByRole(mentorRole);
        List<MentorPerformanceResponse> performanceList = new ArrayList<>();

        for (UserEntity mentor : mentors) {
            List<CourseEntity> courses;
            if (semesterId != null) {
                SemesterEntity semester = semesterRepository.findById(semesterId)
                        .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));
                courses = courseRepository.findByMentorAndSemester(mentor, semester);
            } else {
                courses = courseRepository.findByMentor(mentor);
            }

            long totalStudents = courses.stream()
                    .mapToLong(c -> enrollmentRepository.findByCourse(c).size())
                    .sum();

            long totalTeams = courses.stream()
                    .mapToLong(c -> teamRepository.findByCourse(c).size())
                    .sum();

            List<MentorPerformanceResponse.CoursePerformance> coursePerformances = courses.stream()
                    .map(course -> {
                        long studentsCount = enrollmentRepository.findByCourse(course).size();
                        long teamsCount = teamRepository.findByCourse(course).size();
                        long ideasCount = teamRepository.findByCourse(course).stream()
                                .flatMap(t -> teamMemberRepository.findByTeam(t).stream())
                                .mapToLong(tm -> ideaRepository.findByEnrollment(tm.getEnrollment()).size())
                                .sum();

                        return MentorPerformanceResponse.CoursePerformance.builder()
                                .courseId(course.getCourseId())
                                .courseCode(course.getCode())
                                .courseName(course.getName())
                                .semesterCode(course.getSemester().getCode())
                                .studentsCount(studentsCount)
                                .teamsCount(teamsCount)
                                .ideasCount(ideasCount)
                                .build();
                    })
                    .collect(Collectors.toList());

            double avgStudentsPerCourse = courses.isEmpty() ? 0 : (double) totalStudents / courses.size();
            double avgTeamsPerCourse = courses.isEmpty() ? 0 : (double) totalTeams / courses.size();
            double teamFormationRate = totalStudents > 0
                    ? (totalTeams * 100.0 / totalStudents)
                    : 0;

            MentorProfileEntity profile = mentorProfileRepository.findByUser(mentor).orElse(null);

            performanceList.add(MentorPerformanceResponse.builder()
                    .mentorId(mentor.getUserId())
                    .mentorName(mentor.getFullName())
                    .shortName(profile != null ? profile.getShortName() : "")
                    .totalCourses((long) courses.size())
                    .totalStudents(totalStudents)
                    .totalTeams(totalTeams)
                    .averageStudentsPerCourse(Math.round(avgStudentsPerCourse * 100.0) / 100.0)
                    .averageTeamsPerCourse(Math.round(avgTeamsPerCourse * 100.0) / 100.0)
                    .teamFormationRate(Math.round(teamFormationRate * 100.0) / 100.0)
                    .courses(coursePerformances)
                    .build());
        }

        return performanceList;
    }

    @Override
    public Map<String, Object> getComprehensiveReport(Long semesterId, Long courseId, Long majorId, Long mentorId) {
        Map<String, Object> report = new HashMap<>();

        // Filter courses based on criteria
        List<CourseEntity> courses = filterCourses(semesterId, courseId, mentorId);

        // Overall statistics
        report.put("totalCourses", courses.size());
        report.put("totalEnrollments", courses.stream()
                .mapToLong(c -> enrollmentRepository.findByCourse(c).size()).sum());
        report.put("totalTeams", courses.stream()
                .mapToLong(c -> teamRepository.findByCourse(c).size()).sum());

        // Filter by major if specified
        final List<UserEntity> studentsFilter;
        if (majorId != null) {
            MajorEntity major = majorRepository.findById(majorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Major không tồn tại"));
            studentsFilter = userRepository.findByMajor(major);
            report.put("majorName", major.getName());
        } else {
            studentsFilter = null;
        }

        // Course details with filtering
        List<Map<String, Object>> courseDetails = new ArrayList<>();
        for (CourseEntity course : courses) {
            List<EnrollmentEntity> enrollments = enrollmentRepository.findByCourse(course);

            if (studentsFilter != null) {
                enrollments = enrollments.stream()
                        .filter(e -> studentsFilter.contains(e.getUser()))
                        .collect(Collectors.toList());
            }

            List<TeamEntity> teams = teamRepository.findByCourse(course);
            long studentsInTeams = teams.stream()
                    .flatMap(team -> teamMemberRepository.findByTeam(team).stream())
                    .filter(tm -> studentsFilter == null || studentsFilter.contains(tm.getEnrollment().getUser()))
                    .count();

            Map<String, Object> courseInfo = new HashMap<>();
            courseInfo.put("courseId", course.getCourseId());
            courseInfo.put("courseName", course.getName());
            courseInfo.put("courseCode", course.getCode());
            courseInfo.put("semesterName", course.getSemester().getName());
            courseInfo.put("mentorName", course.getMentor().getFullName());
            courseInfo.put("enrollments", enrollments.size());
            courseInfo.put("teams", teams.size());
            courseInfo.put("studentsInTeams", studentsInTeams);
            courseInfo.put("teamFormationRate", !enrollments.isEmpty()
                    ? Math.round((studentsInTeams * 100.0 / enrollments.size()) * 100.0) / 100.0
                    : 0);

            courseDetails.add(courseInfo);
        }

        report.put("courses", courseDetails);

        // Add filter information
        Map<String, Object> appliedFilters = new HashMap<>();
        if (semesterId != null) {
            semesterRepository.findById(semesterId).ifPresent(semester ->
                appliedFilters.put("semester", semester.getName()));
        }
        if (courseId != null) {
            courseRepository.findById(courseId).ifPresent(course ->
                appliedFilters.put("course", course.getName()));
        }
        if (majorId != null) {
            majorRepository.findById(majorId).ifPresent(major ->
                appliedFilters.put("major", major.getName()));
        }
        if (mentorId != null) {
            userRepository.findById(mentorId).ifPresent(mentor ->
                appliedFilters.put("mentor", mentor.getFullName()));
        }
        report.put("appliedFilters", appliedFilters);

        return report;
    }

    @Override
    public Map<String, Object> exportTeamReport(Long semesterId, Long courseId, String format) {
        Map<String, Object> exportData = new HashMap<>();

        List<CourseEntity> courses = filterCourses(semesterId, courseId, null);
        List<Map<String, Object>> teamData = new ArrayList<>();

        for (CourseEntity course : courses) {
            List<TeamEntity> teams = teamRepository.findByCourse(course);

            for (TeamEntity team : teams) {
                List<TeamMemberEntity> members = teamMemberRepository.findByTeam(team);

                Map<String, Object> teamInfo = new HashMap<>();
                teamInfo.put("teamId", team.getId());
                teamInfo.put("teamName", team.getName());
                teamInfo.put("courseName", course.getName());
                teamInfo.put("courseCode", course.getCode());
                teamInfo.put("semesterName", course.getSemester().getName());
                teamInfo.put("mentorName", course.getMentor().getFullName());
                teamInfo.put("memberCount", members.size());
                teamInfo.put("hasMainIdea", team.getIdea() != null);
                teamInfo.put("mainIdeaName", team.getIdea() != null ? team.getIdea().getName() : "");

                // Leader info
                TeamMemberEntity leader = members.stream()
                        .filter(TeamMemberEntity::getIsLeader)
                        .findFirst()
                        .orElse(null);
                teamInfo.put("leaderName", leader != null ? leader.getEnrollment().getUser().getFullName() : "");

                // Member names
                String memberNames = members.stream()
                        .map(m -> m.getEnrollment().getUser().getFullName())
                        .collect(Collectors.joining(", "));
                teamInfo.put("members", memberNames);

                teamData.add(teamInfo);
            }
        }

        exportData.put("format", format);
        exportData.put("totalTeams", teamData.size());
        exportData.put("data", teamData);
        exportData.put("generatedAt", java.time.LocalDateTime.now().toString());

        return exportData;
    }

    @Override
    public Map<String, Object> exportStudentReport(Long semesterId, Long majorId, Long mentorId, String format) {
        Map<String, Object> exportData = new HashMap<>();

        List<CourseEntity> courses = filterCourses(semesterId, null, mentorId);
        List<UserEntity> studentsFilter = null;

        if (majorId != null) {
            MajorEntity major = majorRepository.findById(majorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Major không tồn tại"));
            studentsFilter = userRepository.findByMajor(major);
        }

        List<Map<String, Object>> studentData = new ArrayList<>();

        for (CourseEntity course : courses) {
            List<EnrollmentEntity> enrollments = enrollmentRepository.findByCourse(course);

            if (studentsFilter != null) {
                final List<UserEntity> finalFilter = studentsFilter;
                enrollments = enrollments.stream()
                        .filter(e -> finalFilter.contains(e.getUser()))
                        .collect(Collectors.toList());
            }

            for (EnrollmentEntity enrollment : enrollments) {
                UserEntity student = enrollment.getUser();

                // Check if student is in a team
                List<TeamMemberEntity> teamMemberships = teamMemberRepository.findByEnrollment(enrollment);
                TeamMemberEntity teamMember = teamMemberships.isEmpty() ? null : teamMemberships.get(0);

                // Count ideas
                long ideaCount = ideaRepository.findByEnrollment(enrollment).size();

                Map<String, Object> studentInfo = new HashMap<>();
                studentInfo.put("studentId", student.getUserId());
                studentInfo.put("studentName", student.getFullName());
                studentInfo.put("studentEmail", student.getEmail());
                studentInfo.put("majorName", student.getMajor() != null ? student.getMajor().getName() : "");
                studentInfo.put("courseName", course.getName());
                studentInfo.put("courseCode", course.getCode());
                studentInfo.put("semesterName", course.getSemester().getName());
                studentInfo.put("mentorName", course.getMentor().getFullName());
                studentInfo.put("hasTeam", teamMember != null);
                studentInfo.put("teamName", teamMember != null ? teamMember.getTeam().getName() : "");
                studentInfo.put("isLeader", teamMember != null && teamMember.getIsLeader());
                studentInfo.put("ideaCount", ideaCount);
                studentInfo.put("enrollmentDate", enrollment.getEnrollmentDate().toString());

                studentData.add(studentInfo);
            }
        }

        exportData.put("format", format);
        exportData.put("totalStudents", studentData.size());
        exportData.put("data", studentData);
        exportData.put("generatedAt", java.time.LocalDateTime.now().toString());

        return exportData;
    }

    private List<CourseEntity> filterCourses(Long semesterId, Long courseId, Long mentorId) {
        if (courseId != null) {
            CourseEntity course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course không tồn tại"));
            return List.of(course);
        }

        if (semesterId != null && mentorId != null) {
            SemesterEntity semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));
            UserEntity mentor = userRepository.findById(mentorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor không tồn tại"));
            return courseRepository.findByMentorAndSemester(mentor, semester);
        }

        if (semesterId != null) {
            SemesterEntity semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester không tồn tại"));
            return courseRepository.findBySemester(semester);
        }

        if (mentorId != null) {
            UserEntity mentor = userRepository.findById(mentorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor không tồn tại"));
            return courseRepository.findByMentor(mentor);
        }

        return courseRepository.findAll();
    }
}

