package com.projectspace.projectspaceapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectspace.projectspaceapi.common.response.Error;
import com.projectspace.projectspaceapi.common.response.ErrorBody;
import com.projectspace.projectspaceapi.common.response.ForbiddenError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ObjectMapper mapper = new ObjectMapper();
        ErrorBody error = new ErrorBody(List.of(new ForbiddenError()));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(error);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }
}
