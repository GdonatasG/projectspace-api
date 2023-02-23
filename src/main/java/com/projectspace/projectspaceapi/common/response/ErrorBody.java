package com.projectspace.projectspaceapi.common.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ErrorBody {
    private boolean success = false;
    private List<Error> errors;

    public ErrorBody(List<Error> errors) {
        this.errors = errors;
    }
}
