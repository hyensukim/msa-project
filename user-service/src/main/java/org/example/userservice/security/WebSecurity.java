package org.example.userservice.security;


import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class WebSecurity{
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;
//    private CustomAuthenticationManager authenticationManager;
    private Environment env;

    // CustomAuthenticationManager authenticationManager,
//

    public WebSecurity(UserService userService,
            BCryptPasswordEncoder passwordEncoder,
                        Environment env) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
        this.env = env;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher( "/favicon.ico"))
                .requestMatchers(new AntPathRequestMatcher( "/css/**"))
                .requestMatchers(new AntPathRequestMatcher( "/js/**"))
                .requestMatchers(new AntPathRequestMatcher( "/img/**"))
                .requestMatchers(new AntPathRequestMatcher( "/lib/**"));
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // 인증 과정에서 사용하기 위한 AuthenticationManager 생성을 위한 로직
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // csrf 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // HTTP 요청에 대한 접근 제어 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/**"))
                .access(new WebExpressionAuthorizationManager("hasIpAddress('127.0.0.1')"))
                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                .anyRequest().authenticated()
        ).authenticationManager(authenticationManager); // 추가 안해주면, SecurityFilterChain 초기화 오류 발생함.

        // 커스텀 인증 필터 추가
        http.addFilter(getAuthenticationFilter(authenticationManager));

        // H2와 같이 아이프레임으로 구분된 html 을 갖는 프로그램에 화면 오류가 나지 않도록 설정을 추가해줘야 함.
        // 클릭재킹 공격을 방지하기 위해서 동일 출처 정책을 활용하여 iframe 을 처리.
        http.headers(h->h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception{
        return new AuthenticationFilter(authenticationManager, userService, env);
    }

}
