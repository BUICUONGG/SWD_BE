package swd.fpt.exegroupingmanagement.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import swd.fpt.exegroupingmanagement.dto.response.TeamResponse;
import swd.fpt.exegroupingmanagement.entity.IdeaEntity;
import swd.fpt.exegroupingmanagement.entity.TeamEntity;
import swd.fpt.exegroupingmanagement.entity.TeamMemberEntity;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public interface TeamMapper {

    default TeamResponse toResponse(TeamEntity entity) {
        if (entity == null) {
            return null;
        }

        return TeamResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .courseId(entity.getCourse() != null ? entity.getCourse().getCourseId() : null)
                .courseName(entity.getCourse() != null ? entity.getCourse().getName() : null)
                .courseCode(entity.getCourse() != null ? entity.getCourse().getCode() : null)
                .semesterId(entity.getCourse() != null && entity.getCourse().getSemester() != null
                        ? entity.getCourse().getSemester().getSemesterId() : null)
                .semesterName(entity.getCourse() != null && entity.getCourse().getSemester() != null
                        ? entity.getCourse().getSemester().getName() : null)
                .mentorId(entity.getCourse() != null && entity.getCourse().getMentor() != null
                        ? entity.getCourse().getMentor().getUserId() : null)
                .mentorName(entity.getCourse() != null && entity.getCourse().getMentor() != null
                        ? entity.getCourse().getMentor().getFullName() : null)
                .memberCount(entity.getTeamMembers() != null ? entity.getTeamMembers().size() : 0)
                .mainIdeaId(entity.getIdea() != null ? entity.getIdea().getId() : null)
                .mainIdeaName(entity.getIdea() != null ? entity.getIdea().getName() : null)
                .members(entity.getTeamMembers() != null
                        ? entity.getTeamMembers().stream()
                                .map(this::toMemberResponse)
                                .collect(Collectors.toList())
                        : List.of())
                .ideas(entity.getTeamMembers() != null
                        ? entity.getTeamMembers().stream()
                                .flatMap(tm -> tm.getEnrollment().getIdeas().stream())
                                .map(idea -> toIdeaResponse(idea, entity.getIdea()))
                                .collect(Collectors.toList())
                        : List.of())
                .build();
    }

    default TeamResponse.TeamMemberResponse toMemberResponse(TeamMemberEntity member) {
        if (member == null || member.getEnrollment() == null) {
            return null;
        }

        return TeamResponse.TeamMemberResponse.builder()
                .enrollmentId(member.getEnrollment().getEnrollmentId())
                .userId(member.getEnrollment().getUser() != null
                        ? member.getEnrollment().getUser().getUserId() : null)
                .userFullName(member.getEnrollment().getUser() != null
                        ? member.getEnrollment().getUser().getFullName() : null)
                .userEmail(member.getEnrollment().getUser() != null
                        ? member.getEnrollment().getUser().getEmail() : null)
                .isLeader(member.getIsLeader())
                .majorName(member.getEnrollment().getUser() != null
                        && member.getEnrollment().getUser().getMajor() != null
                        ? member.getEnrollment().getUser().getMajor().getName() : null)
                .build();
    }

    default TeamResponse.IdeaResponse toIdeaResponse(IdeaEntity idea, IdeaEntity mainIdea) {
        if (idea == null) {
            return null;
        }

        return TeamResponse.IdeaResponse.builder()
                .ideaId(idea.getId())
                .name(idea.getName())
                .description(idea.getDescription())
                .ownerId(idea.getEnrollment() != null && idea.getEnrollment().getUser() != null
                        ? idea.getEnrollment().getUser().getUserId() : null)
                .ownerName(idea.getEnrollment() != null && idea.getEnrollment().getUser() != null
                        ? idea.getEnrollment().getUser().getFullName() : null)
                .isMainIdea(mainIdea != null && idea.getId().equals(mainIdea.getId()))
                .build();
    }

    default List<TeamResponse> toResponseList(List<TeamEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

