package bssm.bsmauth.domain.auth.presentation.dto.req.teacher;

import bssm.bsmauth.domain.auth.presentation.dto.req.UserSignUpReq;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
public class TeacherSignUpReq extends UserSignUpReq {

    @NotBlank
    @Size(
            min = 1,
            max = 8,
            message = "이름은 1 ~ 8글자여야 합니다"
    )
    private String name;
}
