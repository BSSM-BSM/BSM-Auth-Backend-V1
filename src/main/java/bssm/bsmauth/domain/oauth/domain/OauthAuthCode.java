package bssm.bsmauth.domain.oauth.domain;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthAuthCode extends BaseTimeEntity {

    @Id
    @Column(length = 32)
    private String code;

    @Column(length = 8, insertable = false, updatable = false)
    private String clientId;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private OauthClient oauthClient;

    @Column(name = "isExpire", nullable = false)
    @ColumnDefault("0")
    private boolean expire;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne
    @JoinColumn(name = "userCode", insertable = false, updatable = false)
    private User user;

    @Builder
    public OauthAuthCode(String code, String clientId, OauthClient oauthClient, boolean expire, Long userCode, User user) {
        this.code = code;
        this.clientId = clientId;
        this.oauthClient = oauthClient;
        this.expire = expire;
        this.userCode = userCode;
        this.user = user;
    }

    public void expire() {
        this.expire = true;
    }
}
