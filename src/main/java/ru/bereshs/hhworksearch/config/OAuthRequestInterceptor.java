package ru.bereshs.hhworksearch.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.bereshs.hhworksearch.service.AuthorizationClientService;

@Configuration
@Primary
public class OAuthRequestInterceptor implements RequestInterceptor {

    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.application.version}")
    private String appVersion;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";

    private final AuthorizationClientService service;

    @Autowired
    public OAuthRequestInterceptor(AuthorizationClientService service) {
        this.service = service;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String userAgent = appName + "/" + appVersion + " (breshs9@gmail.com)";
        requestTemplate.header("HH-User-Agent", userAgent);
        if (service.getClientToken() != null && service.getClientToken().accessToken() != null) {
            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, service.getClientToken().accessToken()));
        }

    }


}
