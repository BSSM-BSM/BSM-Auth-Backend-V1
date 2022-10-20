package bssm.bsmauth.domain.oauth.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OauthGetResourceRequest extends OauthClientRequest {

    private String token;

}
