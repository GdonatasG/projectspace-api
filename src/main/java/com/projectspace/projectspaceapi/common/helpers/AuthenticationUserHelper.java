package com.projectspace.projectspaceapi.common.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.user.model.User;
import com.projectspace.projectspaceapi.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthenticationUserHelper {
    public static User getCurrentUser(UserService service) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization").split(" ")[1];

        DecodedJWT verify = JWT.require(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes()))
                .build()
                .verify(token.replace(AuthenticationConfigConstants.TOKEN_PREFIX, ""));

        return service.readUserByUsername(verify.getSubject());
    }
}