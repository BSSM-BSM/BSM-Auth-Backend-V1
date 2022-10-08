package bssm.bsmauth.domain.oauth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class OauthGetResourceRequest extends OauthClientRequest {

    @NotBlank
    @Size(
            min = 32,
            max = 32,
            message = "토큰은 32글자여야 합니다"
    )
    private String token;
}
