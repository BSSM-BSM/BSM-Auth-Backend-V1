package bssm.bsmauth.domain.oauth.domain;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthClientScope {

    @EmbeddedId
    private OauthClientScopePk pk;

    @ManyToOne
    @JoinColumn(name = "clientId", insertable = false, updatable = false)
    private OauthClient oauthClient;

    @ManyToOne
    @JoinColumn(name = "scopeId", insertable = false, updatable = false)
    private OauthScope oauthScope;

    @Builder
    public OauthClientScope(OauthClientScopePk pk, OauthClient oauthClient, OauthScope oauthScope) {
        this.pk = pk;
        this.oauthClient = oauthClient;
        this.oauthScope = oauthScope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OauthClientScope)) return false;

        OauthClientScope that = (OauthClientScope) o;

        return pk != null ? pk.equals(that.pk) : that.pk == null;
    }

    @Override
    public int hashCode() {
        return pk != null ? pk.hashCode() : 0;
    }
}
