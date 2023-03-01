package com.projectspace.projectspaceapi.common.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class SuccessBodyList<T> {
    private boolean success = true;
    private List<T> data;

    public SuccessBodyList() {
    }

    public SuccessBodyList(List<T> data) {
        this.data = data;
    }
}
