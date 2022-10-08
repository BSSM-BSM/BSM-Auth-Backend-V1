package bssm.bsmauth.domain.oauth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class OauthAuthorizationRequest {

    @NotBlank
    @Size(min = 8, max = 8)
    private String clientId;

    @NotBlank
    @Size(min = 1, max = 100)
    private String redirectURI;
}
