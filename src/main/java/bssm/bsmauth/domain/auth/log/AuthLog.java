package bssm.bsmauth.domain.auth.log;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.entity.BaseTimeEntity;
import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.StreamUtils;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthLogType type;

    @Column(columnDefinition = "LONGTEXT")
    @Type(JsonStringType.class)
    private final Map<String, Object> content = new HashMap<>();

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_code")
    private User user;

    @Column
    private String ip;

    @Column
    private String requestHost;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String requestUri;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String requestQuery;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String requestHeader;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String requestBody;

    public static AuthLog ofLoginFail(HttpServletRequest rawReq, User user, String id) throws IOException {
        AuthLog authLog = AuthLog.create(rawReq);
        authLog.type = AuthLogType.LOGIN_FAIL;
        authLog.content.put("id", id);
        authLog.user = user;
        return authLog;
    }

    public static AuthLog ofApiTokenFail(HttpServletRequest rawReq) throws IOException {
        AuthLog authLog = AuthLog.create(rawReq);
        authLog.type = AuthLogType.API_TOKEN_FAIL;
        return authLog;
    }

    private static AuthLog create(HttpServletRequest rawReq) throws IOException {
        AuthLog authLog = new AuthLog();
        authLog.createdAt = LocalDateTime.now();
        authLog.ip = rawReq.getHeader("X-Forwarded-For");
        authLog.requestHost = rawReq.getServerName() + ":" + rawReq.getServerPort();
        authLog.requestUri = rawReq.getRequestURI();
        authLog.requestQuery = rawReq.getQueryString();
        authLog.requestHeader = Collections.list(rawReq.getHeaderNames())
                .stream()
                .map(headerName -> headerName + ": " + Collections.list(rawReq.getHeaders(headerName)))
                .collect(Collectors.joining("\n"));
        authLog.requestBody = StreamUtils.copyToString(rawReq.getInputStream(), Charset.defaultCharset());
        return authLog;
    }

}
