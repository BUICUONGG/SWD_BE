package swd.fpt.exegroupingmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatisticsResponse {
    private Long totalUsers;
    private Long totalStudents;
    private Long totalMentors;
    private Long totalCourses;
    private Long totalEnrollments;
    private Long totalTeams;
    private Long totalIdeas;
    private Long activeCourses;
    private Long completedCourses;

    // Statistics by current semester
    private SemesterStatistics currentSemesterStats;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SemesterStatistics {
        private String semesterCode;
        private String semesterName;
        private Long coursesCount;
        private Long enrollmentsCount;
        private Long teamsCount;
        private Double averageTeamSize;
    }
}

