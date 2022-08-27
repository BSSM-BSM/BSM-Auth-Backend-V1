package bssm.bsmauth.global.utils;

import bssm.bsmauth.domain.user.type.UserRole;
import bssm.bsmauth.global.auth.RefreshToken;
import bssm.bsmauth.domain.user.entities.Student;
import bssm.bsmauth.domain.user.entities.User;
import bssm.bsmauth.domain.user.repositories.RefreshTokenRepository;
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
public class JwtUtil {

    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${env.jwt.secretKey}")
    private String JWT_SECRET_KEY;
    @Value("${env.jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${env.jwt.time.refreshToken}")
    private long JWT_REFRESH_TOKEN_MAX_TIME;

    public String createAccessToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("code", user.getCode());
        claims.put("role", user.getRole());
        claims.put("id", user.getId());
        claims.put("nickname", user.getNickname());
        claims.put("studentId", user.getStudentId());
        claims.put("enrolledAt", user.getStudent().getEnrolledAt());
        claims.put("grade", user.getStudent().getGrade());
        claims.put("classNo", user.getStudent().getClassNo());
        claims.put("studentNo", user.getStudent().getStudentNo());
        claims.put("name", user.getStudent().getName());
        return createToken(claims, JWT_TOKEN_MAX_TIME);
    }

    public String createRefreshToken(Long usercode) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String newRandomToken = HexFormat.of().formatHex(randomBytes);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRandomToken)
                .usercode(usercode)
                .isAvailable(true)
                .createdAt(new Date())
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
                .setExpiration(new Date(date.getTime() + time))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("token", String.class);
    }

    public User getUser(String token) {
        Claims claims = extractAllClaims(token);
        Student student = Student.builder()
                .enrolledAt(claims.get("enrolledAt", Integer.class))
                .grade(claims.get("grade", Integer.class))
                .classNo(claims.get("classNo", Integer.class))
                .studentNo(claims.get("studentNo", Integer.class))
                .name(claims.get("name", String.class))
                .build();

        return User.builder()
                .code(claims.get("code", Long.class))
                .role(UserRole.valueOf(claims.get("role", String.class)))
                .id(claims.get("id", String.class))
                .nickname(claims.get("nickname", String.class))
                .student(student)
                .studentId(claims.get("studentId", String.class))
                .build();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
