package com.projectspace.projectspaceapi.projectmember.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteMemberRequest {

    @NotNull(message = "project_id is required.")
    private Long project_id;
    @NotNull(message = "member_id is required.")
    private Long member_id;
}
