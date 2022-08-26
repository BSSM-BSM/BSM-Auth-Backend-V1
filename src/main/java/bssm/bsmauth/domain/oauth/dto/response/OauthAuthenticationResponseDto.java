package bssm.bsmauth.domain.oauth.dto.response;

import bssm.bsmauth.domain.oauth.entities.OauthScope;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OauthAuthenticationResponseDto {

    private boolean authorized;
    private String domain;
    private String serviceName;
    private List<OauthScope> scopeList;
}
