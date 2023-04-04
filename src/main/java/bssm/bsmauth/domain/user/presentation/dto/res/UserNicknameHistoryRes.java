package bssm.bsmauth.domain.user.presentation.dto.res;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.type.UserRole;
import bssm.bsmauth.domain.user.presentation.dto.res.student.StudentRes;
import bssm.bsmauth.domain.user.presentation.dto.res.teacher.TeacherRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserNicknameHistoryRes {

    private Long code;
    private String nickname;
    private LocalDateTime createdAt;
    private String profileUrl;
    private UserRole role;
    private StudentRes student;
    private TeacherRes teacher;
    private List<NicknameHistoryRes> nicknameHistory;

    public static UserNicknameHistoryRes create(User user) {
        UserNicknameHistoryRes res = new UserNicknameHistoryRes();
        res.code = user.getCode();
        res.nickname = user.getNickname();
        res.createdAt = user.getCreatedAt();
        res.profileUrl = user.getProfileUrl();
        res.role = user.getRole();
        res.student = StudentRes.ofUser(user);
        res.teacher = TeacherRes.ofUser(user);
        res.nicknameHistory = user.getNicknameHistories().stream()
                .map(NicknameHistoryRes::create)
                .toList();
        return res;
    }
}
