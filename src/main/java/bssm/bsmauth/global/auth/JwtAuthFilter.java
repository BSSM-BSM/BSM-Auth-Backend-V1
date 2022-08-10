package bssm.bsmauth.global.auth;

import bssm.bsmauth.global.exceptions.HttpError;
import bssm.bsmauth.global.exceptions.HttpErrorResponse;
import bssm.bsmauth.global.exceptions.NotFoundException;
import bssm.bsmauth.global.exceptions.UnAuthorizedException;
import bssm.bsmauth.global.utils.CookieUtil;
import bssm.bsmauth.global.utils.JwtUtil;
import bssm.bsmauth.user.entities.User;
import bssm.bsmauth.user.repositories.RefreshTokenRepository;
import bssm.bsmauth.user.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Value("${TOKEN_COOKIE_NAME}")
    private String TOKEN_COOKIE_NAME;
    @Value("${REFRESH_TOKEN_COOKIE_NAME}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${JWT_TOKEN_MAX_TIME}")
    private long JWT_TOKEN_MAX_TIME;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        Cookie tokenCookie = cookieUtil.getCookie(req, TOKEN_COOKIE_NAME);
        try {
            String token = tokenCookie.getValue();
            authentication(token, req);
        } catch (Exception e) {
            Cookie refreshTokenCookie = cookieUtil.getCookie(req, REFRESH_TOKEN_COOKIE_NAME);
            // 엑세스 토큰 인증에 실패했으면서 리프레시 토큰도 없으면 인증 실패
            if (refreshTokenCookie == null) {
                filterChain.doFilter(req, res);
                return;
            }
            try {
                String refreshToken = refreshTokenCookie.getValue();

                // 리프레시 토큰 만료 기간 확인은 JWT 발급할 때 이미 했으므로 DB에서 사용할 수 있는지 확인
                User user = refreshTokenRepository.findByTokenAndIsAvailable(
                        jwtUtil.getRefreshToken(refreshToken), true
                ).orElseThrow(
                        () -> {throw new NotFoundException("토큰을 찾을 수 없습니다");}
                ).getUser();

                // 새 엑세스 토큰 발급
                String newToken = jwtUtil.createAccessToken(user);
                // 쿠키 생성 및 적용
                Cookie newTokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, newToken, JWT_TOKEN_MAX_TIME);
                res.addCookie(newTokenCookie);

                authentication(newToken, req);
            } catch (NotFoundException e1) {
              throw e1;
            } catch (Exception e1) {
                e1.printStackTrace();
                throw new UnAuthorizedException("다시 로그인 해주세요");
            }
        }
        filterChain.doFilter(req, res);
    }

    private void authentication(String token, HttpServletRequest req) {
        UserDetails userDetails = new UserInfo(jwtUtil.getUser(token));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

