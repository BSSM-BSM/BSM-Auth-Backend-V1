package bssm.bsmauth.oauth.dto.response;

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
}
