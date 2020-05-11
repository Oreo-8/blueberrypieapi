package com.example.blueberrypieapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Component
@Data
public class JwtConfig {
    public static final String REDIS_TOKEN_KEY_PREFIX = "TOKEN_";
    private long time;     // 5天(以秒s计)过期时间
    private String secret;// JWT密码
    private String prefix;         // Token前缀
    private String header; // 存放Token的Header Key
    private String login;
    private long login_time;
}
