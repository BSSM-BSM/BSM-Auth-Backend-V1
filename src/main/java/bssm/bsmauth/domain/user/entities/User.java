package bssm.bsmauth.domain.user.entities;

import bssm.bsmauth.domain.user.type.UserRole;
import bssm.bsmauth.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long code;

    @Column(nullable = false, length = 20, unique = true)
    private String id;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(length = 10)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private Student student;

    @Column
    private Long teacherId;

    @OneToOne
    @JoinColumn(name = "teacherId", insertable = false, updatable = false)
    private Teacher teacher;

    @Column(nullable = false, length = 64)
    private String pw;

    @Column(nullable = false, length = 64)
    private String pwSalt;

    @Builder
    public User(Long code, String id, String nickname, UserRole role, String studentId, Student student, Long teacherId, Teacher teacher, String pw, String pwSalt) {
        this.code = code;
        this.id = id;
        this.nickname = nickname;
        this.role = role;
        this.studentId = studentId;
        this.student = student;
        this.teacherId = teacherId;
        this.teacher = teacher;
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
