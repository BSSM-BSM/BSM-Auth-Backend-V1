package bssm.bsmauth.domain.user.domain;

import bssm.bsmauth.domain.user.domain.type.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "user")
public class UserCache {

    @Id
    private Long id;
    private String authId;
    private String nickname;
    private UserRole role;
    private String studentId;
    private Student student;
    private Long teacherId;
    private Teacher teacher;

    public static UserCache create(User user) {
        UserCache userCache = new UserCache();
        userCache.id = user.getId();
        userCache.authId = user.getAuthId();
        userCache.nickname = user.getNickname();
        userCache.role = user.getRole();
        userCache.studentId = user.getStudentId();
        userCache.student = user.getStudent();
        userCache.teacherId = user.getTeacherId();
        userCache.teacher = user.getTeacher();
        return userCache;
    }

    public User toUser() {
        return User.create(this);
    }

}
