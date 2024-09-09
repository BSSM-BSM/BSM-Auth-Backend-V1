package bssm.bsmauth.global.config;

import bssm.bsmauth.global.auth.AuthFilterExceptionHandler;
import bssm.bsmauth.global.auth.constant.RequestPath;
import bssm.bsmauth.global.error.HttpErrorResponse;
import bssm.bsmauth.global.error.exceptions.UnAuthorizedException;
import bssm.bsmauth.global.auth.AuthFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthFilter authFilter;
    private final AuthFilterExceptionHandler authFilterExceptionHandler;
    private final ObjectMapper objectMapper;

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
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((configure -> {
                    configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }))
                .exceptionHandling(configure -> {
                    configure.authenticationEntryPoint(authenticationEntryPoint());
                })
                .authorizeHttpRequests(configure -> {
                    configure.requestMatchers(RequestPath.excludedAuthTokenPaths.toArray(RequestMatcher[]::new)).permitAll();
                    configure.anyRequest().authenticated();
                });

        http
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authFilterExceptionHandler, AuthFilter.class);

        return http.build();
    }
}