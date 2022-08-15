package bssm.bsmauth.user.entities;

import bssm.bsmauth.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
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
}
