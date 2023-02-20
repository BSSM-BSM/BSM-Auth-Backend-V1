package bssm.bsmauth.domain.oauth.presentation.dto.req;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class OauthAuthorizationReq {

    @NotBlank
    @Size(
            min = 8,
            max = 8,
            message = "clientId는 8글자여야 합니다"
    )
    private String clientId;

    @NotBlank
    @Size(
            min = 1,
            max = 100,
            message = "redirectURI는 1 ~ 100글자여야 합니다"
    )
    private String redirectURI;
}
