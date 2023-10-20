package bssm.bsmauth.domain.user.presentation.dto.res;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.type.UserRole;
import bssm.bsmauth.domain.user.presentation.dto.res.student.StudentRes;
import bssm.bsmauth.domain.user.presentation.dto.res.teacher.TeacherRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRes {

    private Long code;
    private String nickname;
    private String email;
    private LocalDateTime createdAt;
    private String profileUrl;
    private UserRole role;
    private StudentRes student;
    private TeacherRes teacher;

    public static UserRes create(User user) {
        UserRes res = new UserRes();
        res.code = user.getCode();
        res.nickname = user.getNickname();
        res.email = user.findEmailOrNull();
        res.createdAt = user.getCreatedAt();
        res.profileUrl = user.getProfileUrl();
        res.role = user.getRole();
        res.student = StudentRes.ofUser(user);
        res.teacher = TeacherRes.ofUser(user);
        return res;
    }
}
