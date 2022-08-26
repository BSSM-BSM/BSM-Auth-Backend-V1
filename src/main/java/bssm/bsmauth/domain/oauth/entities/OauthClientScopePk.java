package bssm.bsmauth.domain.oauth.entities;


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
}
