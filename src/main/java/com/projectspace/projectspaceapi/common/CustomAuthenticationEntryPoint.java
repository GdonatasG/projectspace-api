package com.projectspace.projectspaceapi.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.Error;
import com.projectspace.projectspaceapi.common.response.ErrorBody;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        String header = request.getHeader(AuthenticationConfigConstants.HEADER_STRING);

        if (header == null) {
            ErrorBody error = new ErrorBody(
                    List.of(
                            new Error("forbidden", "Forbidden!")
                    )
            );

            showErrorResponse(response, error, HttpServletResponse.SC_FORBIDDEN);

            return;
        }

        if (header.startsWith(AuthenticationConfigConstants.TOKEN_PREFIX)) {
            String token = request.getHeader(AuthenticationConfigConstants.HEADER_STRING);

            try {
                JWT.require(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes())).build().verify(token.replace(AuthenticationConfigConstants.TOKEN_PREFIX, "")).getSubject();
            } catch (TokenExpiredException exception) {
                ErrorBody error = new ErrorBody(
                        List.of(
                                new Error("token_expired", "Token expired!")
                        )
                );

                showErrorResponse(response, error, HttpServletResponse.SC_UNAUTHORIZED);
            }

            return;
        }

        ErrorBody error = new ErrorBody(
                List.of(
                        new Error("unauthorized", "Unauthorized!")
                )
        );

        showErrorResponse(response, error, HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void showErrorResponse(HttpServletResponse response, ErrorBody error, int status) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(status);
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(error);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }
}
