package com.projectspace.projectspaceapi.common.response;

public class ForbiddenError extends Error{
    public ForbiddenError(){
        super.setType("forbidden");
        super.setMessage("Forbidden!");
    }
}
