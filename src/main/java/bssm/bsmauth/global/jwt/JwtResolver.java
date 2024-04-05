package bssm.bsmauth.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class JwtResolver {

    @Value("${env.jwt.secret-key.auth}")
    private String JWT_AUTH_SECRET_KEY;

    @Value("${env.jwt.secret-key.api-token}")
    private String JWT_API_TOKEN_SECRET_KEY;

    public String getRefreshToken(String token) {
        Claims claims = extractAllClaims(token, JWT_AUTH_SECRET_KEY);
        return claims.get("token", String.class);
    }

    public Long getUserCode(String token) {
        Claims claims = extractAllClaims(token, JWT_AUTH_SECRET_KEY);
        return claims.get("code", Long.class);
    }

    public ZonedDateTime getClientDateTime(String token) {
        Claims claims = extractAllClaims(token, JWT_API_TOKEN_SECRET_KEY);
        String clientDateTimeStr = claims.get("dateTime", String.class);
        Instant instant = Instant.parse(clientDateTimeStr);
        return instant.atZone(ZoneId.systemDefault());
    }

    private Claims extractAllClaims(String token, String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
