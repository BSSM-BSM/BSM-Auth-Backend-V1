package bssm.bsmauth.domain.auth.presentation.dto.req.teacher;

import lombok.Getter;

import jakarta.validation.constraints.Email;

@Getter
public class TeacherFindIdMailReq {

    @Email(
            message = "올바른 선생님 이메일 주소가 아닙니다"
    )
    private String email;
}
