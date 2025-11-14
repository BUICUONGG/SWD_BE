package swd.fpt.exegroupingmanagement.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeamResponse {
    Long id;
    String name;
    Long courseId;
    String courseName;
    String courseCode;
    Long semesterId;
    String semesterName;
    Long mentorId;
    String mentorName;
    Integer memberCount;
    Long mainIdeaId;
    String mainIdeaName;
    List<TeamMemberResponse> members;
    List<IdeaResponse> ideas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TeamMemberResponse {
        Long enrollmentId;
        Long userId;
        String userFullName;
        String userEmail;
        Boolean isLeader;
        String majorName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class IdeaResponse {
        Long ideaId;
        String name;
        String description;
        Long ownerId;
        String ownerName;
        Boolean isMainIdea;
    }
}

