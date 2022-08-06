package bssm.bsmauth.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OauthAuthenticationResponseDto {

    private boolean isAuthorized;
    private String domain;
    private String serviceName;
    private List<String> scopeList;
}
