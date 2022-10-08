package bssm.bsmauth.domain.oauth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class OauthClientRequest {

    @NotBlank
    @Size(min = 8, max = 8)
    private String clientId;

    @NotBlank
    @Size(min = 32, max = 32)
    private String clientSecret;
}
