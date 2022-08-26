package bssm.bsmauth.domain.user.entities;

import bssm.bsmauth.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResetPwToken extends BaseTimeEntity {

    @Id
    @Column(length = 32)
    private String token;

    @Column(name = "isUsed", nullable = false)
    private boolean used;

    @Column(nullable = false)
    private Date expireIn;

    @Column(columnDefinition = "INT UNSIGNED")
    private int usercode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    private User user;

    @Builder
    public ResetPwToken(String token, boolean used, Date expireIn, int usercode, User user) {
        this.token = token;
        this.used = used;
        this.expireIn = expireIn;
        this.usercode = usercode;
        this.user = user;
    }

    public void setUsed(boolean isUsed) {
        this.used = isUsed;
    }
}
