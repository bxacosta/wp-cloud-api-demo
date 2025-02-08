package com.bxacosta.wpcloudapi.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "facebook")
public class FacebookAuthConfig {
    private String appId;
    private String configId;
    private String appSecret;
    private String webhookToken;
    private String callbackUrl;
}
