package bssm.bsmauth.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UserSignUpRequest {

    @NotBlank
    @Size(min = 2, max = 20)
    private String id;

    @NotBlank
    @Size(min = 8, max = 24)
    private String pw;

    @NotBlank
    @Size(min = 8, max = 24)
    private String checkPw;

    @NotBlank
    @Size(min = 1, max = 20)
    private String nickname;

    @NotBlank
    @Size(min = 6, max = 6)
    private String authCode;
}
