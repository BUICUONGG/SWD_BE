package swd.fpt.exegroupingmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import swd.fpt.exegroupingmanagement.dto.response.StandardResponse;
import static swd.fpt.exegroupingmanagement.dto.response.StandardResponse.success;
import swd.fpt.exegroupingmanagement.service.ReportService;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Report Management", description = "APIs for admin to view statistics and reports")
public class ReportController {
    ReportService reportService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get dashboard overview",
               description = "Admin views overall system statistics")
    public ResponseEntity<StandardResponse<Object>> getDashboard() {
        return ResponseEntity.ok(success("Lấy dashboard thành công",
                reportService.getDashboardStatistics()));
    }

    @GetMapping("/students/by-semester/{semesterId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get student report by semester",
               description = "Admin views student statistics for a specific semester")
    public ResponseEntity<StandardResponse<Object>> getStudentsBySemester(
            @PathVariable Long semesterId) {
        return ResponseEntity.ok(success("Lấy báo cáo học sinh theo kỳ học thành công",
                reportService.getStudentReportBySemester(semesterId)));
    }

    @GetMapping("/students/by-mentor/{mentorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get student report by mentor",
               description = "Admin views student statistics for a specific mentor/teacher")
    public ResponseEntity<StandardResponse<Object>> getStudentsByMentor(
            @PathVariable Long mentorId,
            @RequestParam(required = false) Long semesterId) {
        return ResponseEntity.ok(success("Lấy báo cáo học sinh theo giáo viên thành công",
                reportService.getStudentReportByMentor(mentorId, semesterId)));
    }

    @GetMapping("/students/by-major/{majorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get student report by major",
               description = "Admin views student statistics for a specific major")
    public ResponseEntity<StandardResponse<Object>> getStudentsByMajor(
            @PathVariable Long majorId,
            @RequestParam(required = false) Long semesterId) {
        return ResponseEntity.ok(success("Lấy báo cáo học sinh theo ngành thành công",
                reportService.getStudentReportByMajor(majorId, semesterId)));
    }

    @GetMapping("/courses/statistics")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get course statistics",
               description = "Admin views statistics for all courses")
    public ResponseEntity<StandardResponse<Object>> getCourseStatistics(
            @RequestParam(required = false) Long semesterId) {
        return ResponseEntity.ok(success("Lấy thống kê khóa học thành công",
                reportService.getCourseStatistics(semesterId)));
    }

    @GetMapping("/teams/statistics")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get team statistics",
               description = "Admin views statistics for all teams")
    public ResponseEntity<StandardResponse<Object>> getTeamStatistics(
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long courseId) {
        return ResponseEntity.ok(success("Lấy thống kê nhóm thành công",
                reportService.getTeamStatistics(semesterId, courseId)));
    }

    @GetMapping("/enrollments/statistics")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get enrollment statistics",
               description = "Admin views enrollment statistics by semester, course, or major")
    public ResponseEntity<StandardResponse<Object>> getEnrollmentStatistics(
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long majorId) {
        return ResponseEntity.ok(success("Lấy thống kê đăng ký thành công",
                reportService.getEnrollmentStatistics(semesterId, majorId)));
    }

    @GetMapping("/mentors/performance")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get mentor performance report",
               description = "Admin views performance metrics for all mentors")
    public ResponseEntity<StandardResponse<Object>> getMentorPerformance(
            @RequestParam(required = false) Long semesterId) {
        return ResponseEntity.ok(success("Lấy báo cáo hiệu suất giáo viên thành công",
                reportService.getMentorPerformanceReport(semesterId)));
    }

    @GetMapping("/comprehensive")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get comprehensive report with filters",
               description = "Admin views comprehensive system report filtered by semester, course, major, or mentor")
    public ResponseEntity<StandardResponse<Object>> getComprehensiveReport(
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long majorId,
            @RequestParam(required = false) Long mentorId) {
        return ResponseEntity.ok(success("Lấy báo cáo tổng hợp thành công",
                reportService.getComprehensiveReport(semesterId, courseId, majorId, mentorId)));
    }

    @GetMapping("/export/teams")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Export team report",
               description = "Export team statistics to Excel/CSV filtered by semester or course")
    public ResponseEntity<StandardResponse<Object>> exportTeamReport(
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "excel") String format) {
        return ResponseEntity.ok(success("Xuất báo cáo nhóm thành công",
                reportService.exportTeamReport(semesterId, courseId, format)));
    }

    @GetMapping("/export/students")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Export student report",
               description = "Export student statistics to Excel/CSV filtered by semester, major or mentor")
    public ResponseEntity<StandardResponse<Object>> exportStudentReport(
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long majorId,
            @RequestParam(required = false) Long mentorId,
            @RequestParam(defaultValue = "excel") String format) {
        return ResponseEntity.ok(success("Xuất báo cáo học sinh thành công",
                reportService.exportStudentReport(semesterId, majorId, mentorId, format)));
    }
}

