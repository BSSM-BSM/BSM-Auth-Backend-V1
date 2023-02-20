package bssm.bsmauth.domain.oauth.presentation.dto.req;

import lombok.Getter;

@Getter
public class OauthGetResourceReq extends OauthClientReq {

    private String token;

}
