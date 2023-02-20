package bssm.bsmauth.domain.oauth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthAuthorizationRes {

    private String redirectURI;
}
