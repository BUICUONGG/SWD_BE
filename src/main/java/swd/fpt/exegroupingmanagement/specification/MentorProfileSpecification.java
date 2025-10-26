package swd.fpt.exegroupingmanagement.specification;

import org.springframework.data.jpa.domain.Specification;

import swd.fpt.exegroupingmanagement.entity.MentorProfileEntity;

public class MentorProfileSpecification {

    public static Specification<MentorProfileEntity> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("shortName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("fullName")), likePattern)
            );
        };
    }
}

