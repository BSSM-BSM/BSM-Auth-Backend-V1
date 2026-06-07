package bssm.bsmauth.domain.auth.presentation.dto.req.student;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class UpdateStudentRecoveryEmailReq {

    @Email(message = "올바른 이메일 주소가 아닙니다")
    private String newRecoveryEmail;
}
