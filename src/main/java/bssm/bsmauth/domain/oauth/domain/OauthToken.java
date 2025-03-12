package bssm.bsmauth.domain.oauth.domain;

import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.domain.user.domain.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthToken extends BaseTimeEntity {

    @Id
    @Column(length = 32)
    private String token;

    @Column(length = 8, insertable = false, updatable = false)
    private String clientId;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private OauthClient oauthClient;

    @Column(name = "isExpire", nullable = false)
    @ColumnDefault("0")
    private boolean expire;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Builder
    public OauthToken(String token, String clientId, OauthClient oauthClient, boolean expire, User user) {
        this.token = token;
        this.clientId = clientId;
        this.oauthClient = oauthClient;
        this.expire = expire;
        this.user = user;
    }
}
