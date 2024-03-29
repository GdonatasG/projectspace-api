package com.projectspace.projectspaceapi.common.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class SuccessBody<T> {
    private boolean success = true;
    private T data;

    public SuccessBody() {
    }

    public SuccessBody(T data) {
        this.data = data;
    }

}
