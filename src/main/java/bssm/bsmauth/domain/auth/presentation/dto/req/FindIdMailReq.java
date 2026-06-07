package bssm.bsmauth.domain.auth.presentation.dto.req;

import lombok.Getter;

import jakarta.validation.constraints.Email;

@Getter
public class FindIdMailReq {

    @Email(message = "올바른 이메일 주소가 아닙니다")
    private String email;
}
