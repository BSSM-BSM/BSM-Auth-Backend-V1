package bssm.bsmauth.global.auth.constant;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

public class RequestPath {

    public static final List<RequestMatcher> ignoringPaths = Arrays.asList(
            new AntPathRequestMatcher("/auth/login", HttpMethod.POST.toString()),
            new AntPathRequestMatcher("/auth/mail/**", HttpMethod.POST.toString()),
            new AntPathRequestMatcher("/auth/pw/token"),

            new AntPathRequestMatcher("/auth/student", HttpMethod.POST.toString()),
            new AntPathRequestMatcher("/auth/teacher", HttpMethod.POST.toString()),

            new AntPathRequestMatcher("/oauth/token", HttpMethod.POST.toString()),
            new AntPathRequestMatcher("/oauth/resource", HttpMethod.POST.toString())
    );
}