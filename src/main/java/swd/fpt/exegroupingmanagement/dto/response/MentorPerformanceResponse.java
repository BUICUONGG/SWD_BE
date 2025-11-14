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
public class MentorPerformanceResponse {
    private Long mentorId;
    private String mentorName;
    private String shortName;

    private Long totalCourses;
    private Long totalStudents;
    private Long totalTeams;
    private Double averageStudentsPerCourse;
    private Double averageTeamsPerCourse;
    private Double teamFormationRate;

    private List<CoursePerformance> courses;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CoursePerformance {
        private Long courseId;
        private String courseCode;
        private String courseName;
        private String semesterCode;
        private Long studentsCount;
        private Long teamsCount;
        private Long ideasCount;
    }
}

