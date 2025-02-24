package org.milkys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 *  Spring Security의 설정 클래스로, 애플리케이션의 보안 정책을 설정하는 부분
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .headers().disable()
                .httpBasic().disable()
                .logout().disable()
                .formLogin().disable()
                .authorizeRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}
/**
 *@Configuration: 이 클래스가 Spring의 설정 클래스임을 나타냅니다.
 *  Spring Container에 의해 관리되는 설정 정보를 포함하고 있습니다.
 * @EnableWebSecurity: Spring Security 기능을 활성화하는 어노테이션입니다.
 * 이 어노테이션을 사용하면 Spring Security가 기본적인 보안 설정을 적용합니다.
 *
 * @Bean: SecurityFilterChain 객체를 Spring Bean으로 등록하여 Spring Context에서 사용할 수 있도록 합니다.
 * SecurityFilterChain: Spring Security에서 요청에 대해 필터를 설정하는 객체입니다. 여기서 요청에 대한 보안 설정을 정의합니다.
 * HttpSecurity: HTTP 요청에 대한 보안 설정을 할 수 있는 객체입니다. 이를 통해 인증, 권한, CSRF 설정 등을 할 수 있습니다.
 */