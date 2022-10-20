package bssm.bsmauth.domain.oauth.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthAuthorizationResponseDto {

    private String redirectURI;
}
