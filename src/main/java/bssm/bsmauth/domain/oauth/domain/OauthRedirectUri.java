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
    private OauthRedirectUriPk pk;

    @ManyToOne
    @JoinColumn(name = "clientId", insertable = false, updatable = false)
    private OauthClient oauthClient;

    @Builder
    public OauthRedirectUri(OauthRedirectUriPk pk, OauthClient oauthClient) {
        this.pk = pk;
        this.oauthClient = oauthClient;
    }

    public String toUriString() {
        return pk.getRedirectUri();
    }
}
