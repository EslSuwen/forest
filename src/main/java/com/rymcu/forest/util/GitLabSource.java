package com.rymcu.forest.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * GitLabSource gitlab 第三方登录私服
 *
 * @author 黄富灵
 * @date 2020/12/28 16:15
 */
@Component
public class GitLabSource {
    public static String authorize;
    public static String accessToken;
    public static String userInfo;
    public static String clientId;
    public static String secret;
    public static String redirectUrl;

    @Value("${third.authorize}")
    public void setAuthorize(String authorize) {
        GitLabSource.authorize = authorize;
    }

    @Value("${third.accessToken}")
    public void setAccessToken(String accessToken) {
        GitLabSource.accessToken = accessToken;
    }

    @Value("${third.userInfo}")
    public void setUserInfo(String userInfo) {
        GitLabSource.userInfo = userInfo;
    }

    @Value("${justauth.type.GITLAB.client-id}")
    public void setClientId(String clientId) {
        GitLabSource.clientId = clientId;
    }

    @Value("${justauth.type.GITLAB.client-secret}")
    public void setSecret(String secret) {
        GitLabSource.secret = secret;
    }

    @Value("${justauth.type.GITLAB.redirect-uri}")
    public void setRedirectUrl(String redirectUrl) {
        GitLabSource.redirectUrl = redirectUrl;
    }
}
