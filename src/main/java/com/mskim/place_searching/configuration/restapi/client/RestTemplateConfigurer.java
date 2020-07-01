package com.mskim.place_searching.configuration.restapi.client;

import com.mskim.place_searching.configuration.restapi.client.support.KakaoRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestTemplateConfigurer {
    @Value("${kakao.client.key}")
    private String kakaoClientKey;

    @Bean
    public KakaoRestTemplate kakaoMapRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder.defaultHeader("Content-type", "application/json;charset=UTF-8")
                .defaultHeader("Authorization", "KakaoAK " + kakaoClientKey)
                .build(KakaoRestTemplate.class);
    }
}
