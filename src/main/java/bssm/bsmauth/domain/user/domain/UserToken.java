package bssm.bsmauth.domain.user.domain;

import bssm.bsmauth.global.entity.BaseTimeEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
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

    @Column(name = "isUsed", nullable = false)
    private boolean used;

    @Column(nullable = false)
    private Date expireIn;

    @Column(length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserTokenType type;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long usercode;

    @ManyToOne
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    private User user;

    public void setUsed(boolean isUsed) {
        this.used = isUsed;
    }
}
