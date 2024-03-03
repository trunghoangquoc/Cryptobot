package com.ai.cryptobot.common;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author ADMIN
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

    public static final String APPLICATION_JSON_VALUE = "application/json";
    public static final String APPLICATION_ACCESS_TOKEN = "access_token";
    public static final String APPLICATION_REFRESH_TOKEN = "refresh_token";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String LOGIN_URL = "/api/login";
    public static final String REFRESH_TOKEN_URL = "/api/refreshToken";

}
