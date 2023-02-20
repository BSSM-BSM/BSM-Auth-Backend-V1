package bssm.bsmauth.domain.oauth.presentation.dto.req;

import lombok.Getter;

@Getter
public class OauthGetTokenReq extends OauthClientReq {

    private String authCode;

}
