package swd.fpt.exegroupingmanagement.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.entity.CourseEntity;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;
import swd.fpt.exegroupingmanagement.repository.CourseRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseStatusScheduler {
    CourseRepository courseRepository;

    /**
     * Tự động chuyển Course từ UPCOMING sang OPEN khi semester.startDate đến
     * Chạy mỗi giờ
     */
    @Scheduled(cron = "0 0 * * * ?") // Chạy mỗi giờ
    @Transactional
    public void updateUpcomingToOpen() {
        LocalDate now = LocalDate.now();
        List<CourseEntity> coursesToOpen = courseRepository.findUpcomingCoursesToOpen(
                CourseStatus.UPCOMING, now);

        if (!coursesToOpen.isEmpty()) {
            log.info("🔄 Đang chuyển {} course từ UPCOMING sang OPEN", coursesToOpen.size());
            
            for (CourseEntity course : coursesToOpen) {
                course.setStatus(CourseStatus.OPEN);
                
                // Set teamFormationDeadline = 1 tuần sau khi OPEN
                LocalDateTime deadline = LocalDateTime.now().plusWeeks(1);
                course.setTeamFormationDeadline(deadline);
                
                log.info("✅ Course [{}] chuyển từ UPCOMING → OPEN, deadline ghép nhóm: {}", 
                        course.getCode(), deadline);
            }
            
            courseRepository.saveAll(coursesToOpen);
            log.info("✅ Đã cập nhật {} course sang trạng thái OPEN", coursesToOpen.size());
        }
    }

    /**
     * Tự động chuyển Course từ OPEN sang IN_PROGRESS sau khi hết deadline ghép nhóm
     * Chạy mỗi giờ
     */
    @Scheduled(cron = "0 0 * * * ?") // Chạy mỗi giờ
    @Transactional
    public void updateOpenToInProgress() {
        LocalDateTime now = LocalDateTime.now();
        List<CourseEntity> coursesToStart = courseRepository.findOpenCoursesToStart(
                CourseStatus.OPEN, now);

        if (!coursesToStart.isEmpty()) {
            log.info("🔄 Đang chuyển {} course từ OPEN sang IN_PROGRESS", coursesToStart.size());
            
            for (CourseEntity course : coursesToStart) {
                course.setStatus(CourseStatus.IN_PROGRESS);
                log.info("✅ Course [{}] chuyển từ OPEN → IN_PROGRESS", course.getCode());
            }
            
            courseRepository.saveAll(coursesToStart);
            log.info("✅ Đã cập nhật {} course sang trạng thái IN_PROGRESS", coursesToStart.size());
        }
    }

    /**
     * Tự động chuyển Course từ IN_PROGRESS sang COMPLETED khi semester.endDate đến
     * Chạy mỗi giờ
     */
    @Scheduled(cron = "0 0 * * * ?") // Chạy mỗi giờ
    @Transactional
    public void updateInProgressToCompleted() {
        LocalDate now = LocalDate.now();
        List<CourseEntity> coursesToComplete = courseRepository.findInProgressCoursesToComplete(
                CourseStatus.IN_PROGRESS, now);

        if (!coursesToComplete.isEmpty()) {
            log.info("🔄 Đang chuyển {} course từ IN_PROGRESS sang COMPLETED", coursesToComplete.size());
            
            for (CourseEntity course : coursesToComplete) {
                course.setStatus(CourseStatus.COMPLETED);
                log.info("✅ Course [{}] chuyển từ IN_PROGRESS → COMPLETED", course.getCode());
            }
            
            courseRepository.saveAll(coursesToComplete);
            log.info("✅ Đã cập nhật {} course sang trạng thái COMPLETED", coursesToComplete.size());
        }
    }
}

