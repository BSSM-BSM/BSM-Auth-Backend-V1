package bssm.bsmauth.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class LoginRequest {

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
}
