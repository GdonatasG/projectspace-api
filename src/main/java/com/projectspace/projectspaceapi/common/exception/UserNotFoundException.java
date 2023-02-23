package com.projectspace.projectspaceapi.common.exception;

public class UserNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Wrong credentials!";
    }
}
