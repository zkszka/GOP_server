package com.mysite.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mysite.login.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors() // CORS 설정
                .and()
            .csrf().disable() // CSRF 비활성화
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // OPTIONS 요청 허용
                .antMatchers(HttpMethod.POST, "/api/login").permitAll() // 로그인 API 허용
                .antMatchers("/api/join").permitAll() // 회원가입 API 허용
                .antMatchers(HttpMethod.GET, "/api/dogs/crawl").permitAll() // 특정 GET 요청 허용
                .antMatchers(HttpMethod.GET, "/api/dogs/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/cats/all").permitAll()
                .antMatchers(HttpMethod.POST, "/api/cats/crawl").permitAll()
                .antMatchers(HttpMethod.GET, "/api/cats/27").permitAll()
                .antMatchers(HttpMethod.GET, "/api/check-session").authenticated() // 세션 체크 API 인증 필요
                .antMatchers(HttpMethod.GET, "/api/missing/all").permitAll() // 모든 사용자에게 실종 동물 조회 API 허용
                .antMatchers(HttpMethod.POST, "/api/posts").authenticated()
                .anyRequest().authenticated() // 모든 요청 인증 필요
                .and()
            .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID") // 쿠키 삭제
                .logoutSuccessUrl("/login?logout=true") // 로그아웃 후 리다이렉트 URL
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 인증 실패 시 401 에러
                .accessDeniedPage("/error") // 권한 부족 시 에러 페이지
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션이 필요한 경우에만 생성
                .sessionFixation().newSession() // 세션 고정 공격 방지
                .invalidSessionUrl("/custom-login?error=sessionExpired") // 세션 만료 시 리다이렉트 URL
                .maximumSessions(1) // 최대 세션 수 설정
                .maxSessionsPreventsLogin(true) // 최대 세션 수 초과 시 로그인 방지
                .expiredUrl("/custom-login?error=sessionExpired"); // 세션 만료 시 리다이렉트 URL
    }

}
