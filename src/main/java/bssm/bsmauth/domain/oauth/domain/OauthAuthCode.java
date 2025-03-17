package bssm.bsmauth.domain.oauth.domain;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("is_expired != true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthAuthCode extends BaseTimeEntity {

    @Id
    @Column(length = 32)
    private String code;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private OauthClient oauthClient;

    @Column(name = "is_expired", nullable = false)
    @ColumnDefault("0")
    private boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public OauthAuthCode(String code, OauthClient oauthClient, boolean isExpired, User user) {
        this.code = code;
        this.oauthClient = oauthClient;
        this.isExpired = isExpired;
        this.user = user;
    }

    public void expire() {
        this.isExpired = true;
    }
}
