package bssm.bsmauth.oauth.entities;

import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.user.entities.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class OauthToken extends BaseTimeEntity {

    @Id
    @Column(length = 32)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId")
    private OauthClient oauthClient;

    @Column(name = "isExpire", nullable = false)
    @ColumnDefault("0")
    private boolean expire;

    @Column(columnDefinition = "INT UNSIGNED")
    private int usercode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    private User user;
}
