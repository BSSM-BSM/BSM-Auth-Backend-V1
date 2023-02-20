package bssm.bsmauth.domain.user.presentation.dto.res;

import bssm.bsmauth.domain.user.domain.UserRole;
import bssm.bsmauth.domain.user.presentation.dto.res.student.StudentRes;
import bssm.bsmauth.domain.user.presentation.dto.res.teacher.TeacherRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherUserRes {

    private Long code;
    private String nickname;
    private LocalDateTime createdAt;
    private UserRole role;
    private StudentRes student;
    private TeacherRes teacher;

}