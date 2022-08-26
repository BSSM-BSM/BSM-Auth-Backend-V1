package bssm.bsmauth.domain.user.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int usercode;

    @Column(nullable = false, length = 20, unique = true)
    private String id;

    @Column(nullable = false, length = 40, unique = true)
    private String nickname;

    @Column(nullable = false, length = 10)
    private String uniqNo;

    @OneToOne
    @JoinColumn(name = "uniqNo", insertable = false, updatable = false)
    private Student student;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false, length = 64)
    private String pw;

    @Column(nullable = false, length = 64)
    private String pwSalt;

    @Builder
    public User(int usercode, String id, String nickname, String uniqNo, Student student, int level, Date createdAt, String pw, String pwSalt) {
        this.usercode = usercode;
        this.id = id;
        this.nickname = nickname;
        this.uniqNo = uniqNo;
        this.student = student;
        this.level = level;
        this.createdAt = createdAt;
        this.pw = pw;
        this.pwSalt = pwSalt;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setPwSalt(String salt) {
        this.pwSalt = salt;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
