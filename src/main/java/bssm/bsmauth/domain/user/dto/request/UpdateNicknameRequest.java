package bssm.bsmauth.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UpdateNicknameRequest {

    @NotBlank
    @Size(
            min = 1,
            max = 20,
            message = "닉네임은 1 ~ 20글자여야 합니다"
    )
    private String newNickname;
}
