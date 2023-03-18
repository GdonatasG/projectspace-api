package com.projectspace.projectspaceapi.projectmember.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProjectMemberRequest {
    @NotNull(message = "member_id is required.")
    private Long member_id;

    @Nullable
    private Long member_level_id;
}
