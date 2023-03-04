package com.projectspace.projectspaceapi.projectmember.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteMemberRequest {

    @NotNull(message = "projectId is required.")
    private Long projectId;
    @NotNull(message = "memberId is required.")
    private Long memberId;
}
