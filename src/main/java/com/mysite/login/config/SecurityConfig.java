package com.mysite.login.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mysite.login.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()) // CORS 설정
            .and().csrf().disable() // CSRF 비활성화
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // OPTIONS 요청 허용
                .antMatchers(HttpMethod.POST, "/api/login").permitAll() // 로그인 API 허용
                .antMatchers(HttpMethod.POST, "/api/join").permitAll() // 회원가입 API 허용
                .antMatchers(HttpMethod.POST, "/api/request-password-reset").permitAll() // 비밀번호 재설정 요청 API 허용
                .antMatchers(HttpMethod.POST, "/api/reset-password").permitAll() // 비밀번호 재설정 API 허용
                .antMatchers(HttpMethod.POST, "/api/dogs/crawl").permitAll()
                .antMatchers(HttpMethod.GET, "/api/dogs/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/cats/all").permitAll()
                .antMatchers(HttpMethod.POST, "/api/cats/crawl").permitAll()
                .antMatchers(HttpMethod.GET, "/api/cats/27").permitAll()
                .antMatchers(HttpMethod.GET, "/api/check-session").authenticated() // 세션 체크 API 인증 필요
                .antMatchers(HttpMethod.GET, "/api/missing/all").permitAll() // 실종 동물 조회 API 허용
                .antMatchers(HttpMethod.POST, "/api/posts").authenticated() // 게시물 추가 API 인증 필요
                .antMatchers("/api/admin/**").hasRole("ADMIN") // ADMIN 권한만 접근 가능

                .antMatchers("/api/v1/oauth2/google", "/api/v1/oauth2/google/callback").permitAll() // 구글 로그인 관련 경로 허용
                .antMatchers("/favicon.ico").permitAll()


                .anyRequest().authenticated() // 나머지 모든 요청 인증 필요
            .and().logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)) // 로그아웃 성공 시 상태 코드 설정
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID") // 쿠키 삭제
                .logoutSuccessUrl("/login?logout=true") // 로그아웃 후 리다이렉트 URL
            .and().exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 인증 실패 시 401 에러
                .accessDeniedPage("/error") // 권한 부족 시 에러 페이지
            .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션이 필요한 경우에만 생성
                .sessionFixation().newSession() // 세션 고정 공격 방지
                .invalidSessionUrl("/custom-login?error=sessionExpired") // 세션 만료 시 리다이렉트 URL
                .maximumSessions(1) // 최대 세션 수 설정
                .maxSessionsPreventsLogin(true) // 최대 세션 수 초과 시 로그인 방지
                .expiredUrl("/custom-login?error=sessionExpired"); // 세션 만료 시 리다이렉트 URL
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://web-gopprj-m128ei6pc6510b38.sel4.cloudtype.app")); // 클라우드 서버 URL로 변경
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
