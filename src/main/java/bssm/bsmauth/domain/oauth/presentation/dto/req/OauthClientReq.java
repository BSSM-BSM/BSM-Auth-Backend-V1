package bssm.bsmauth.domain.oauth.presentation.dto.req;

import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
public class OauthClientReq {

    @NotBlank
    @Size(
            min = 8,
            max = 8,
            message = "clientId는 8글자여야 합니다"
    )
    private String clientId;

    @NotBlank
    @Size(
            min = 32,
            max = 32,
            message = "clientSecret은 32글자여야 합니다"
    )
    private String clientSecret;
}
