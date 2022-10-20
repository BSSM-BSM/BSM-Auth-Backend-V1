package bssm.bsmauth.domain.user.presentation.dto.response;

import bssm.bsmauth.domain.user.domain.UserRole;
import bssm.bsmauth.domain.user.presentation.dto.response.student.StudentInfoResponse;
import bssm.bsmauth.domain.user.presentation.dto.response.teacher.TeacherInfoResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

    private Long code;
    private String nickname;
    private String email;
    private LocalDateTime createdAt;
    private UserRole role;
    private StudentInfoResponse student;
    private TeacherInfoResponse teacher;

}
