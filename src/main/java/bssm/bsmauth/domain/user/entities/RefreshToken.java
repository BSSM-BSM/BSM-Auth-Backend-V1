package bssm.bsmauth.domain.user.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(length = 64)
    private String token;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(columnDefinition = "INT UNSIGNED")
    private int usercode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private Date createdAt;

    @Builder
    public RefreshToken(String token, boolean isAvailable, int usercode, User user, Date createdAt) {
        this.token = token;
        this.isAvailable = isAvailable;
        this.usercode = usercode;
        this.user = user;
        this.createdAt = createdAt;
    }
}
