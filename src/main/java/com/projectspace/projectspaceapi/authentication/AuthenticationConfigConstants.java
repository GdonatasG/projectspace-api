package com.projectspace.projectspaceapi.authentication;

public class AuthenticationConfigConstants {
    public static final String SECRET = "Secret_Of_ProjectSpace_JWT_Authentication";
    public static final long EXPIRATION_TIME_IN_MS = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static final String URL_PREFIX = "/api";
    public static final String USER_URL = URL_PREFIX + "/user";
    public static final String PROJECT_URL = URL_PREFIX + "/project";
    public static final String PROJECT_MEMBER_URL = PROJECT_URL + "/members";
    public static final String PROJECT_MEMBER_LEVEL_URL = PROJECT_URL + "/member-levels";
    public static final String PROJECT_INVITATIONS_URL = PROJECT_URL + "/invitations";
    public static final String TASK_STATUS_URL = URL_PREFIX + "/task/status";

    public static final String TASK_PRIORITY_URL = URL_PREFIX + "/task/priority";


}
