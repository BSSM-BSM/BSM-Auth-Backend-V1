package bssm.bsmauth.domain.oauth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthTokenRes {

    private String token;
}
