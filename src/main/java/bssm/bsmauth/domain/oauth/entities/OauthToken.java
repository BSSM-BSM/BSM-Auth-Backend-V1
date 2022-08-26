package bssm.bsmauth.domain.oauth.entities;

import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.domain.user.entities.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthToken extends BaseTimeEntity {

    @Id
    @Column(length = 32)
    private String token;

    @Column(length = 8, insertable = false, updatable = false)
    private String clientId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId")
    private OauthClient oauthClient;

    @Column(name = "isExpire", nullable = false)
    @ColumnDefault("0")
    private boolean expire;

    @Column(columnDefinition = "INT UNSIGNED")
    private int usercode;

    @ManyToOne
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    private User user;

    @Builder
    public OauthToken(String token, String clientId, OauthClient oauthClient, boolean expire, int usercode, User user) {
        this.token = token;
        this.clientId = clientId;
        this.oauthClient = oauthClient;
        this.expire = expire;
        this.usercode = usercode;
        this.user = user;
    }
}
