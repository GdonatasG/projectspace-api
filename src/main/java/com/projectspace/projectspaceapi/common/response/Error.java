package com.projectspace.projectspaceapi.common.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Error {
    private String type;
    private String message;

    public Error(String type, String message) {
        this.type = type;
        this.message = message;
    }
}