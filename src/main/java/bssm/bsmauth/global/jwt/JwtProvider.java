package bssm.bsmauth.global.jwt;

import bssm.bsmauth.domain.auth.domain.RefreshToken;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.auth.domain.repository.RefreshTokenRepository;
import bssm.bsmauth.domain.user.facade.UserFacade;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HexFormat;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserFacade userFacade;

    @Value("${jwt.secret-key.auth}")
    private String JWT_AUTH_SECRET_KEY;
    @Value("${jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${jwt.time.refresh-token}")
    private long JWT_REFRESH_TOKEN_MAX_TIME;

    public String createAccessToken(User user) {
        userFacade.saveUserCache(user);

        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        return createToken(claims, JWT_TOKEN_MAX_TIME);
    }

    public String createRefreshToken(User user) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String newRandomToken = HexFormat.of().formatHex(randomBytes);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRandomToken)
                .user(user)
                .isAvailable(true)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        Claims claims = Jwts.claims();
        claims.put("token", newRandomToken);
        return createToken(claims, JWT_REFRESH_TOKEN_MAX_TIME);
    }

    private String createToken(Claims claims, long time) {
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + (time * 1000)))
                .signWith(Keys.hmacShaKeyFor(JWT_AUTH_SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }
}
