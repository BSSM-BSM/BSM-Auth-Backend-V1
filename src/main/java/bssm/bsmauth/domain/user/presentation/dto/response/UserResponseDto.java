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
public class UserResponseDto {

    private Long code;
    private String nickname;
    private UserRole role;
    private LocalDateTime createdAt;
    private StudentInfoResponse student;
    private TeacherInfoResponse teacher;
}
