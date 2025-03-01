package com.bxacosta.wpcloudapi.auth;

import com.restfb.AccessToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FacebookAuthController {

    private final FacebookAuthService facebookAuthService;

    @GetMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Facebook login");

        String requestUrl = request.getRequestURL().toString();
        String url = facebookAuthService.getLoginUrl(requestUrl);

        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam String code,
            @RequestParam String state,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Facebook callback");

        AccessToken accessToken = facebookAuthService.exchangeCodeForAccessToken(code);

        log.info("Facebook access token: {}", accessToken);
        request.getSession().setAttribute("access_token", accessToken.getAccessToken());

        URL url = URI.create(state).toURL();
        response.sendRedirect(String.format("%s://%s", url.getProtocol(), url.getHost()));
    }
}
