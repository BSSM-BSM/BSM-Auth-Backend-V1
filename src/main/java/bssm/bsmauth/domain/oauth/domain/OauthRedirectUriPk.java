package bssm.bsmauth.domain.oauth.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@Embeddable
public class OauthRedirectUriPk implements Serializable {

    @Column(length = 8)
    private String clientId;

    @Column(length = 100)
    private String redirectUri;

    @Builder
    public OauthRedirectUriPk(String clientId, String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }
}
