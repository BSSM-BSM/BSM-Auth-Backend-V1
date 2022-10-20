package bssm.bsmauth.domain.oauth.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthClientScope {

    @EmbeddedId
    private OauthClientScopePk oauthClientScopePk;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId", insertable = false, updatable = false)
    private OauthClient oauthClient;

    @ManyToOne
    @JoinColumn(name = "scopeId", insertable = false, updatable = false)
    private OauthScope oauthScope;

    @Builder
    public OauthClientScope(OauthClientScopePk oauthClientScopePk, OauthClient oauthClient, OauthScope oauthScope) {
        this.oauthClientScopePk = oauthClientScopePk;
        this.oauthClient = oauthClient;
        this.oauthScope = oauthScope;
    }
}
