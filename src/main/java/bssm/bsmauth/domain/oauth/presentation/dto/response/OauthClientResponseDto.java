package bssm.bsmauth.domain.oauth.presentation.dto.response;

import bssm.bsmauth.domain.oauth.domain.OauthClient;
import bssm.bsmauth.domain.oauth.domain.OauthRedirectUri;
import bssm.bsmauth.domain.oauth.domain.type.OauthAccessType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OauthClientResponseDto {

    private String clientId;
    private String clientSecret;
    private String domain;
    private String serviceName;
    private List<String> redirectUriList;
    private List<String> scopeList;
    private OauthAccessType access;
}
