package bssm.bsmauth.domain.oauth.domain;

import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthToken extends BaseTimeEntity {

    @Id
    @Column(length = 32)
    private String token;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private OauthClient oauthClient;

    @Column(name = "is_expire", nullable = false)
    @ColumnDefault("0")
    private boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public OauthToken(String token, OauthClient oauthClient, boolean isExpired, User user) {
        this.token = token;
        this.oauthClient = oauthClient;
        this.isExpired = isExpired;
        this.user = user;
    }
}
