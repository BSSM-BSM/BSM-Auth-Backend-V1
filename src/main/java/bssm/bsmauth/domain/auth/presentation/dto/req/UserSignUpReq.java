package bssm.bsmauth.domain.auth.presentation.dto.req;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserSignUpReq {

    @NotBlank
    @Size(
            min = 2,
            max = 20,
            message = "id는 2 ~ 20글자여야 합니다"
    )
    private String id;

    @NotBlank
    @Size(
            min = 8,
            max = 24,
            message = "비밀번호는 8 ~ 24글자여야 합니다"
    )
    private String pw;

    @NotBlank
    @Size(
            min = 8,
            max = 24,
            message = "비밀번호는 8 ~ 24글자여야 합니다"
    )
    private String checkPw;

    @NotBlank
    @Size(
            min = 1,
            max = 20,
            message = "닉네임은 1 ~ 20글자여야 합니다"
    )
    private String nickname;

    @NotBlank
    private String authCode;
}
