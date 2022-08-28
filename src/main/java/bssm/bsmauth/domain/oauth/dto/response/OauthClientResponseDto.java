package bssm.bsmauth.domain.oauth.dto.response;

import bssm.bsmauth.domain.oauth.type.OauthAccessType;
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
    private String redirectURI;
    private List<String> scopeList;
    private OauthAccessType access;
}
