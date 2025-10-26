package swd.fpt.exegroupingmanagement.specification;

import org.springframework.data.jpa.domain.Specification;

import swd.fpt.exegroupingmanagement.entity.UserEntity;

public class UserSpecification {

    public static Specification<UserEntity> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), likePattern)
            );
        };
    }
}

