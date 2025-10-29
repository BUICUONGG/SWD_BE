package swd.fpt.exegroupingmanagement.specification;

import org.springframework.data.jpa.domain.Specification;

import swd.fpt.exegroupingmanagement.entity.CourseEntity;
import swd.fpt.exegroupingmanagement.enums.CourseStatus;

public class CourseSpecification {

    public static Specification<CourseEntity> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern)
            );
        };
    }

    public static Specification<CourseEntity> hasStatus(CourseStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<CourseEntity> hasSemesterId(Long semesterId) {
        return (root, query, criteriaBuilder) -> {
            if (semesterId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("semester").get("semesterId"), semesterId);
        };
    }

    public static Specification<CourseEntity> hasMentorId(Long mentorId) {
        return (root, query, criteriaBuilder) -> {
            if (mentorId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("mentor").get("userId"), mentorId);
        };
    }

    public static Specification<CourseEntity> hasSubjectId(Long subjectId) {
        return (root, query, criteriaBuilder) -> {
            if (subjectId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("subject").get("subjectId"), subjectId);
        };
    }
}

