package com.projectspace.projectspaceapi.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Nullable
    @Size(min = 1, max = 100, message = "The first name should not be empty and should not exceed 100 characters.")
    @JsonProperty("first_name")
    private String first_name;

    @Nullable
    @Size(min = 1, max = 100, message = "The last name should not be empty and should not exceed 100 characters.")
    @JsonProperty("last_name")
    private String last_name;

    @Nullable
    @Size(max = 100, message = "The organization name should not be empty and should not exceed 100 characters.")
    @JsonProperty("organization_name")
    private String organization_name;
}
