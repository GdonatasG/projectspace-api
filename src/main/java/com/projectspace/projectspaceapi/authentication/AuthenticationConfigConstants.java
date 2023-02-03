package com.projectspace.projectspaceapi.authentication;

public class AuthenticationConfigConstants {
    public static final String SECRET = "Secret_Of_ProjectSpace_JWT_Authentication";
    public static final long EXPIRATION_TIME_IN_MS = /*864000000*/ 300000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/user";
}
