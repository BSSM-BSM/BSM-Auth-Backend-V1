package bssm.bsmauth.domain.oauth.domain;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.global.utils.SecurityUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("is_used != true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthAuthCode extends BaseTimeEntity {

    @Id
    @Column(length = 32)
    private String code;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private OauthClient oauthClient;

    @Column(name = "is_used", nullable = false)
    @ColumnDefault("0")
    private boolean isUsed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public static OauthAuthCode create(OauthClient oauthClient, User user) {
        OauthAuthCode authCode = new OauthAuthCode();
        authCode.code = SecurityUtil.getRandomString(32);
        authCode.oauthClient = oauthClient;
        authCode.isUsed = false;
        authCode.user = user;
        return authCode;
    }

    public void use() {
        this.isUsed = true;
    }
}
