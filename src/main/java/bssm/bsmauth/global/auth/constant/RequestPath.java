package bssm.bsmauth.global.auth.constant;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

public class RequestPath {

    public static final List<RequestMatcher> excludedAuthTokenPaths = Arrays.asList(
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/auth/login"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/auth/mail/**"),
            PathPatternRequestMatcher.pathPattern("/auth/pw/token"),

            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/auth/student"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/auth/teacher"),

            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/oauth/token"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/oauth/resource")
    );

    public static final List<RequestMatcher> excludedApiTokenPaths = Arrays.asList(
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/oauth/token"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/oauth/resource")
    );
}