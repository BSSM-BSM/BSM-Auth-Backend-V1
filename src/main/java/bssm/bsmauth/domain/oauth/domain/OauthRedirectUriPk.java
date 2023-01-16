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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OauthRedirectUriPk)) return false;

        OauthRedirectUriPk that = (OauthRedirectUriPk) o;

        if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) return false;
        return redirectUri != null ? redirectUri.equals(that.redirectUri) : that.redirectUri == null;
    }

    @Override
    public int hashCode() {
        int result = clientId != null ? clientId.hashCode() : 0;
        result = 31 * result + (redirectUri != null ? redirectUri.hashCode() : 0);
        return result;
    }
}
