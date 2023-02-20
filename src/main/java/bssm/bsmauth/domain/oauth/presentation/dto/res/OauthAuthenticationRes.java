package bssm.bsmauth.domain.oauth.presentation.dto.res;

import bssm.bsmauth.domain.oauth.domain.OauthScope;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OauthAuthenticationRes {

    private boolean authorized;
    private String domain;
    private String serviceName;
    private List<OauthScope> scopeList;
}
