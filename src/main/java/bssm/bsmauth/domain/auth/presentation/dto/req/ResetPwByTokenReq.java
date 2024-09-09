package bssm.bsmauth.domain.auth.presentation.dto.req;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
public class ResetPwByTokenReq {

    @NotBlank
    private String token;

    @NotBlank
    @Size(
            min = 8,
            max = 24,
            message = "비밀번호는 8 ~ 24글자여야 합니다"
    )
    private String newPw;

    @NotBlank
    @Size(
            min = 8,
            max = 24,
            message = "비밀번호는 8 ~ 24글자여야 합니다"
    )
    private String checkNewPw;
}
