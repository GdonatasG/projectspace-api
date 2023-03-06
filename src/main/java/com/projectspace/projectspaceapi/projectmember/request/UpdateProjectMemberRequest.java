package com.projectspace.projectspaceapi.projectmember.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProjectMemberRequest {
    @NotNull(message = "memberId is required.")
    private Long memberId;

    @Nullable
    private Long memberLevelId;
}
