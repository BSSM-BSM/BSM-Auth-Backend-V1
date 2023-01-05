package bssm.bsmauth.domain.oauth.domain;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@Embeddable
public class OauthClientScopePk implements Serializable {

    @Column(length = 8)
    private String clientId;

    @Column(length = 16)
    private String scopeId;

    @Builder
    public OauthClientScopePk(String clientId, String scopeId) {
        this.clientId = clientId;
        this.scopeId = scopeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OauthClientScopePk)) return false;

        OauthClientScopePk that = (OauthClientScopePk) o;

        if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) return false;
        return scopeId != null ? scopeId.equals(that.scopeId) : that.scopeId == null;
    }

    @Override
    public int hashCode() {
        int result = clientId != null ? clientId.hashCode() : 0;
        result = 31 * result + (scopeId != null ? scopeId.hashCode() : 0);
        return result;
    }
}
