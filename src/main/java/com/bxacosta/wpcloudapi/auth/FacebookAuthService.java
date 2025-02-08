package com.bxacosta.wpcloudapi.auth;

import com.restfb.AccessToken;
import com.restfb.DefaultFacebookClient;
import com.restfb.Version;
import com.restfb.types.DebugTokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacebookAuthService {

    private final FacebookAuthConfig facebookAuthConfig;

    public String getLoginUrl(String state) {
        DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.LATEST);

        return facebookClient.getBusinessLoginDialogUrl(
                facebookAuthConfig.getAppId(),
                facebookAuthConfig.getCallbackUrl(),
                facebookAuthConfig.getConfigId(),
                state
        );
    }

    public AccessToken exchangeCodeForAccessToken(String code) {
        Objects.requireNonNull(code);

        DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.LATEST);

        AccessToken accessToken = facebookClient.obtainUserAccessToken(
                facebookAuthConfig.getAppId(),
                facebookAuthConfig.getAppSecret(),
                facebookAuthConfig.getCallbackUrl(),
                code
        );

        Objects.requireNonNull(accessToken.getAccessToken());

        return accessToken;
    }

    public DebugTokenInfo debugToken(String inputToken, String accessToken) {
        DefaultFacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);

        return facebookClient.debugToken(inputToken);
    }
}
