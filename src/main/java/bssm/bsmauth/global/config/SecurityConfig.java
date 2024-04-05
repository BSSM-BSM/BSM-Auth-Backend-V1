package bssm.bsmauth.global.config;

import bssm.bsmauth.global.auth.AuthFilterExceptionHandler;
import bssm.bsmauth.global.error.HttpErrorResponse;
import bssm.bsmauth.global.error.exceptions.UnAuthorizedException;
import bssm.bsmauth.global.auth.AuthFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthFilter authFilter;
    private final AuthFilterExceptionHandler authFilterExceptionHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/auth/pw/token")
                .antMatchers(HttpMethod.POST, "/auth/student", "/auth/teacher", "/auth/login", "/auth/pw/token", "/auth/mail/**")
                .antMatchers(HttpMethod.POST, "/oauth/token", "/oauth/resource");
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (req, res, e) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write(objectMapper.writeValueAsString(new HttpErrorResponse(new UnAuthorizedException())));
            res.getWriter().flush();
            res.getWriter().close();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable();

        http
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authFilterExceptionHandler, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}