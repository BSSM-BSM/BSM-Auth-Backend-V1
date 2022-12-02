package bssm.bsmauth.domain.oauth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthRedirectUri {

    @EmbeddedId
    private OauthRedirectUriPk oauthClientScopePk;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId", insertable = false, updatable = false)
    private OauthClient oauthClient;

    @Builder
    public OauthRedirectUri(OauthRedirectUriPk oauthClientScopePk, OauthClient oauthClient) {
        this.oauthClientScopePk = oauthClientScopePk;
        this.oauthClient = oauthClient;
    }

    public String toUriString() {
        return oauthClientScopePk.getRedirectUri();
    }
}
