package bssm.bsmauth.domain.oauth.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class RemoveOauthClientRedirectRequest {

    @NotBlank
    private String clientId;

    @NotBlank
    private String redirectUri;
}
