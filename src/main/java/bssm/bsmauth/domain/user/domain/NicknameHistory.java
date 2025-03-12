package bssm.bsmauth.domain.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, length = 20)
    private String nickname;

    private LocalDateTime modifiedAt;

    public static NicknameHistory create(User user, String nickname) {
        NicknameHistory nicknameHistory = new NicknameHistory();
        nicknameHistory.user = user;
        nicknameHistory.nickname = nickname;
        nicknameHistory.modifiedAt = LocalDateTime.now();
        return nicknameHistory;
    }
}
