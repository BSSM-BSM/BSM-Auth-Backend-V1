package bssm.bsmauth.domain.auth.domain;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.entity.BaseTimeEntity;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("is_available = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {

    @Id
    @Column(length = 64)
    private String token;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public RefreshToken(String token, boolean isAvailable, User user) {
        this.token = token;
        this.isAvailable = isAvailable;
        this.user = user;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
