package org.milkys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // 프론트엔드 주소
                .allowedMethods("PUT", "POST", "GET", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true); // 크리덴셜(쿠키, 인증 헤더 등)을 허용
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 로컬 경로 (C:/milkysDatabase/)를 "/media/**"로 매핑
        registry.addResourceHandler("/media/**")
                .addResourceLocations("file:///C:/milkysDatabase/");
    }
}
