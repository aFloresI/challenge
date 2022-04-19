package com.user.registration.challenge.config;

import com.user.registration.challenge.error.IPApiErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${ip.api.url}")
    private String ipApiUrl;

    @Bean
    RestTemplate ipAPIRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.rootUri(ipApiUrl).errorHandler(new IPApiErrorHandler()).build();
    }

}
