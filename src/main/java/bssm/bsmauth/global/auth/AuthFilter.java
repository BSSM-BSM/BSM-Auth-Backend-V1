package bssm.bsmauth.global.auth;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.facade.UserFacade;
import bssm.bsmauth.global.auth.constant.RequestPath;
import bssm.bsmauth.global.auth.exception.ExpiredAuthTokenException;
import bssm.bsmauth.global.auth.exception.InvalidApiTokenException;
import bssm.bsmauth.global.cookie.CookieProvider;
import bssm.bsmauth.global.jwt.JwtProvider;
import bssm.bsmauth.global.jwt.JwtResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final UserFacade userFacade;
    private final JwtProvider jwtProvider;
    private final JwtResolver jwtResolver;
    private final CookieProvider cookieProvider;
    private final AuthDetailsService authDetailsService;

    @Value("${env.cookie.name.token}")
    private String TOKEN_COOKIE_NAME;
    @Value("${env.cookie.name.refreshToken}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${env.header.name.api-token}")
    private String HEADER_NAME_API_TOKEN;

    @Value("${env.jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${env.jwt.time.api-token}")
    private long API_TOKEN_MAX_TIME;

    @Override
    protected void doFilterInternal(HttpServletRequest rawReq, HttpServletResponse rawRes, FilterChain filterChain) throws ServletException, IOException {
        for (RequestMatcher requestMatcher : RequestPath.excludedApiTokenPaths) {
            if (requestMatcher.matches(rawReq)) {
                filterChain.doFilter(rawReq, rawRes);
                return;
            }
        }
        checkApiToken(rawReq);

        for (RequestMatcher requestMatcher : RequestPath.excludedAuthTokenPaths) {
            if (requestMatcher.matches(rawReq)) {
                filterChain.doFilter(rawReq, rawRes);
                return;
            }
        }
        try {
            accessTokenCheck(rawReq);
        } catch (Exception e) {
            refreshTokenCheck(rawReq, rawRes);
        }
        filterChain.doFilter(rawReq, rawRes);
    }

    private void checkApiToken(HttpServletRequest rawReq) throws IOException {
        String apiToken = rawReq.getHeader(HEADER_NAME_API_TOKEN);
        ZonedDateTime clientRequestDateTime;
        ZonedDateTime serverTime = ZonedDateTime.now();
        try {
            clientRequestDateTime = jwtResolver.getClientDateTime(apiToken);
        } catch (Exception e) {
            throw new InvalidApiTokenException();
        }
        ZonedDateTime maxRequestDateTime = clientRequestDateTime.plusSeconds(API_TOKEN_MAX_TIME);
        ZonedDateTime minRequestDateTime = clientRequestDateTime.minusSeconds(API_TOKEN_MAX_TIME);
        if (serverTime.isBefore(minRequestDateTime) || serverTime.isAfter(maxRequestDateTime)) {
            throw new InvalidApiTokenException(serverTime, clientRequestDateTime);
        }
    }

    private void accessTokenCheck(HttpServletRequest rawReq) {
        Cookie tokenCookie = cookieProvider.findCookie(rawReq, TOKEN_COOKIE_NAME);
        String token = tokenCookie.getValue();
        authentication(token);
    }

    private void authentication(String token) {
        Long userCode = jwtResolver.getUserCode(token);
        UserDetails userDetails = authDetailsService.loadUserByUsername(String.valueOf(userCode));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void refreshTokenCheck(HttpServletRequest rawReq, HttpServletResponse res) {
        Cookie refreshTokenCookie = cookieProvider.findCookie(rawReq, REFRESH_TOKEN_COOKIE_NAME);
        // 엑세스 토큰 인증에 실패했으면서 리프레시 토큰도 없으면 인증 실패
        if (refreshTokenCookie == null) {
            res.addHeader(HttpHeaders.SET_COOKIE, cookieProvider.createCookie(TOKEN_COOKIE_NAME, "", 0).toString());
            return;
        }
        try {
            String refreshToken = jwtResolver.getRefreshToken(refreshTokenCookie.getValue());
            // DB에서 사용할 수 있는지 확인
            User user = userFacade.findByRefreshToken(refreshToken);

            // 새 엑세스 토큰 발급
            String newToken = jwtProvider.createAccessToken(user);
            // 쿠키 생성 및 적용
            ResponseCookie newTokenCookie = cookieProvider.createCookie(TOKEN_COOKIE_NAME, newToken, JWT_TOKEN_MAX_TIME);
            res.addHeader(HttpHeaders.SET_COOKIE, newTokenCookie.toString());

            authentication(newToken);
        } catch (Exception e) {
            res.addHeader(HttpHeaders.SET_COOKIE, cookieProvider.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0).toString());
            res.addHeader(HttpHeaders.SET_COOKIE, cookieProvider.createCookie(TOKEN_COOKIE_NAME, "", 0).toString());
            throw new ExpiredAuthTokenException();
        }
    }

}

