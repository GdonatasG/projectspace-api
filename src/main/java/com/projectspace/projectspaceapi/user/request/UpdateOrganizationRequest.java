package com.projectspace.projectspaceapi.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateOrganizationRequest {
    @Nullable
    @Size(min = 1, max = 100, message = "The organization name should not be empty and should not exceed 100 characters.")
    @JsonProperty("organization_name")
    private String organizationName;
}
