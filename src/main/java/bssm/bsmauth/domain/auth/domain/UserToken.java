package bssm.bsmauth.domain.auth.domain;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.global.utils.SecurityUtil;
import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class UserToken extends BaseTimeEntity {

    @Id
    @Column(length = 32)
    private String token;

    @Column(name = "is_used", nullable = false)
    private boolean used;

    @Column(nullable = false)
    private Date expireIn;

    @Column(length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserTokenType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void expire() {
        this.used = true;
    }

    public static UserToken ofResetPw(User user) {
        UserToken userToken = new UserToken();
        userToken.token = SecurityUtil.getRandomString(32);
        userToken.user = user;
        userToken.used = false;
        userToken.type = UserTokenType.RESET_PW;
        Date expireIn = new Date();
        expireIn.setTime(expireIn.getTime() + (5 * 60 * 1000));
        userToken.expireIn = expireIn;
        return userToken;
    }

    public static UserToken ofNormal(User user) {
        UserToken userToken = new UserToken();
        userToken.token = SecurityUtil.getRandomString(32);
        userToken.user = user;
        userToken.used = false;
        userToken.type = UserTokenType.RESET_PW;
        Date expireIn = new Date();
        expireIn.setTime(expireIn.getTime() + (5 * 60 * 1000));
        userToken.expireIn = expireIn;
        return userToken;
    }

}
