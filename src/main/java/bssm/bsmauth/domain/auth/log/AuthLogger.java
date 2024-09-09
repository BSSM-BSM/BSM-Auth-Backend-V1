package bssm.bsmauth.domain.auth.log;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.facade.UserFacade;
import bssm.bsmauth.global.cookie.CookieProvider;
import bssm.bsmauth.global.jwt.JwtResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthLogger {

    private final UserFacade userFacade;
    private final JwtResolver jwtResolver;
    private final CookieProvider cookieProvider;

    private final AuthLogRepository authLogRepository;

    @Value("${env.cookie.name.token}")
    private String TOKEN_COOKIE_NAME;
    @Value("${env.cookie.name.refreshToken}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${env.header.name.api-token}")
    private String HEADER_NAME_API_TOKEN;

    public void recordLoginFailLog(HttpServletRequest rawReq, User user, String id, String reason) throws IOException {
        AuthLog authLog = AuthLog.ofLoginFail(rawReq, user, id);
        authLog.getContent().put("reason", reason);
        authLogRepository.save(authLog);
    }

    public void recordApiTokenFailLog(HttpServletRequest rawReq) throws IOException {
        AuthLog authLog = AuthLog.ofApiTokenFail(rawReq);

        try {
            String accessToken = cookieProvider.findCookie(rawReq, TOKEN_COOKIE_NAME).getValue();
            Long userCode = jwtResolver.getUserCode(accessToken);
            User user = userFacade.findByCode(userCode);
            authLog.setUser(user);
            authLog.getContent().put("accessToken", accessToken);
        } catch (Exception ignored) {}

        try {
            Cookie refreshTokenCookie = cookieProvider.findCookie(rawReq, REFRESH_TOKEN_COOKIE_NAME);
            String refreshToken = jwtResolver.getRefreshToken(refreshTokenCookie.getValue());
            authLog.getContent().put("refreshToken", refreshToken);

            User user = userFacade.findByRefreshToken(refreshToken);
            authLog.setUser(user);
        } catch (Exception ignored) {}

        String apiToken = rawReq.getHeader(HEADER_NAME_API_TOKEN);
        authLog.getContent().put("apiToken", apiToken);

        authLogRepository.save(authLog);
    }
}
