package com.projectspace.projectspaceapi.common.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SuccessBody {
    private boolean success = true;
}
