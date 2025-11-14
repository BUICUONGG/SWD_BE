package swd.fpt.exegroupingmanagement.service;

import org.springframework.stereotype.Service;
import swd.fpt.exegroupingmanagement.dto.response.DashboardStatisticsResponse;
import swd.fpt.exegroupingmanagement.dto.response.MentorPerformanceResponse;
import swd.fpt.exegroupingmanagement.dto.response.StudentReportResponse;

import java.util.List;
import java.util.Map;

@Service
public interface ReportService {
    DashboardStatisticsResponse getDashboardStatistics();

    StudentReportResponse getStudentReportBySemester(Long semesterId);

    StudentReportResponse getStudentReportByMentor(Long mentorId, Long semesterId);

    StudentReportResponse getStudentReportByMajor(Long majorId, Long semesterId);

    Map<String, Object> getCourseStatistics(Long semesterId);

    Map<String, Object> getTeamStatistics(Long semesterId, Long courseId);

    Map<String, Object> getEnrollmentStatistics(Long semesterId, Long majorId);

    List<MentorPerformanceResponse> getMentorPerformanceReport(Long semesterId);

    Map<String, Object> getComprehensiveReport(Long semesterId, Long courseId, Long majorId, Long mentorId);

    Map<String, Object> exportTeamReport(Long semesterId, Long courseId, String format);

    Map<String, Object> exportStudentReport(Long semesterId, Long majorId, Long mentorId, String format);
}

