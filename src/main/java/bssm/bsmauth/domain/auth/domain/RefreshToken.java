package bssm.bsmauth.domain.auth.domain;

import bssm.bsmauth.domain.user.domain.User;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;

@Getter
@Entity
@SQLRestriction("is_available = true")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(length = 64)
    private String token;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private Date createdAt;

    @Builder
    public RefreshToken(String token, boolean isAvailable, User user, Date createdAt) {
        this.token = token;
        this.isAvailable = isAvailable;
        this.user = user;
        this.createdAt = createdAt;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
