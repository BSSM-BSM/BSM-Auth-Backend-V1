package bssm.bsmauth.domain.user.domain;

import bssm.bsmauth.domain.oauth.domain.OauthAuthCode;
import bssm.bsmauth.domain.user.domain.type.UserRole;
import bssm.bsmauth.domain.user.exception.NoSuchUserEmailException;
import bssm.bsmauth.domain.user.exception.NoSuchUserNameException;
import bssm.bsmauth.domain.user.presentation.dto.res.OtherUserRes;
import bssm.bsmauth.domain.user.presentation.dto.res.UserRes;
import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.global.utils.SecurityUtil;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column
    private String profileUrl;

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

    @Column(name = "failed_login_attempts", nullable = false)
    private Short failedLoginAttempts;

    @OrderBy("modifiedAt DESC")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<NicknameHistory> nicknameHistories = new ArrayList<>();

    public String findEmailOrNull() {
        if (this.role == UserRole.STUDENT) {
            return this.student.getEmail();
        }
        if (this.role == UserRole.TEACHER) {
            return this.teacher.getEmail();
        }
        throw new NoSuchUserEmailException();
    }

    public String findNameOrNull() {
        if (this.role == UserRole.STUDENT) {
            return this.student.getName();
        }
        if (this.role == UserRole.TEACHER) {
            return this.teacher.getName();
        }
        throw new NoSuchUserNameException();
    }

    public boolean validatePw(String pw) {
        String encryptedPw = SecurityUtil.encryptPw(this.pwSalt, pw);
        return this.pw.equals(encryptedPw);
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }

    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
    }

    public boolean checkAccountLock() {
        Short MAX_LOGIN_ATTEMPTS = 5;
        return this.failedLoginAttempts >= MAX_LOGIN_ATTEMPTS;
    }

    public void unlockAccount() {
        this.failedLoginAttempts = 0;
    }

    public void updatePw(String pw) {
        // 비밀번호 솔트 값 생성
        String pwSalt = SecurityUtil.getRandomString(64);
        this.pw = SecurityUtil.encryptPw(pwSalt, pw);
        this.pwSalt = pwSalt;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public UserCache toUserCache() {
        return UserCache.create(this);
    }

    public static User create(UserCache userCache) {
        User user = new User();
        user.code = userCache.getCode();
        user.id = userCache.getId();
        user.nickname = userCache.getNickname();
        user.role = userCache.getRole();
        user.studentId = userCache.getStudentId();
        user.student = userCache.getStudent();
        user.teacherId = userCache.getTeacherId();
        user.teacher = userCache.getTeacher();
        return user;
    }

    private static User createUser(String id, String pw, String nickname) {
        User user = new User();
        user.id = id;
        user.updatePw(pw);
        user.updateNickname(nickname);
        user.failedLoginAttempts = 0;
        return user;
    }

    public static User createStudent(Student student, String id, String pw, String nickname) {
        User user = createUser(id, pw, nickname);
        user.student = student;
        user.studentId = student.getStudentId();
        user.role = UserRole.STUDENT;
        return user;
    }

    public static User createTeacher(Teacher teacher, String id, String pw, String nickname) {
        User user = createUser(id, pw, nickname);
        user.teacher = teacher;
        user.teacherId = teacher.getTeacherId();
        user.role = UserRole.TEACHER;
        return user;
    }

}
