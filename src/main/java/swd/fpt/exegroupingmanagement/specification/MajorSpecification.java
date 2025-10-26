package swd.fpt.exegroupingmanagement.specification;

import org.springframework.data.jpa.domain.Specification;

import swd.fpt.exegroupingmanagement.entity.MajorEntity;

public class MajorSpecification {

    public static Specification<MajorEntity> hasKeyword(String keyword) {
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
}

