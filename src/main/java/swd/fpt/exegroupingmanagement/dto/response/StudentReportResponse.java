package swd.fpt.exegroupingmanagement.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentReportResponse {
    private String reportType; // BY_SEMESTER, BY_MENTOR, BY_MAJOR
    private String filterName;
    private Long filterValue;

    private Long totalStudents;
    private Long enrolledStudents;
    private Long studentsInTeams;
    private Long studentsWithoutTeams;

    private List<CourseStudentSummary> courseDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CourseStudentSummary {
        private Long courseId;
        private String courseCode;
        private String courseName;
        private String mentorName;
        private Long enrolledCount;
        private Long teamsCount;
        private Long studentsInTeams;
        private Double teamFormationRate; // % students in teams
    }
}

