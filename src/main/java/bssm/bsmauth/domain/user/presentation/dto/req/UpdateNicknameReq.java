package bssm.bsmauth.domain.user.presentation.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
public class UpdateNicknameReq {

    @NotBlank
    @Size(
            min = 1,
            max = 20,
            message = "닉네임은 1 ~ 20글자여야 합니다"
    )
    @Pattern(
            regexp = "[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9| -/|:-@|\\[-`|{-~]+$",
            message = "닉네임은 한, 영, 숫자, 특수문자만 들어가야 합니다"
    )
    private String newNickname;

    public String getNewNickname() {
        return newNickname.strip();
    }
}
