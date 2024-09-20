package ru.bereshs.hhworksearch.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration("customRequestInterceptor")
public class CustomRequestInterceptor implements RequestInterceptor {
    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.application.version}")
    private String appVersion;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String userAgent = appName + "/" + appVersion + " (breshs9@gmail.com)";
        requestTemplate.header("HH-User-Agent", userAgent);
    }
}
