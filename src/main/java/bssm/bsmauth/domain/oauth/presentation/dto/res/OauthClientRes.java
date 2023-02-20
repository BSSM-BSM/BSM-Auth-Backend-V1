package bssm.bsmauth.domain.oauth.presentation.dto.res;

import bssm.bsmauth.domain.oauth.domain.type.OauthAccessType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OauthClientRes {

    private String clientId;
    private String clientSecret;
    private String domain;
    private String serviceName;
    private List<String> redirectUriList;
    private List<String> scopeList;
    private OauthAccessType access;
}
