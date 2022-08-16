package bssm.bsmauth.oauth.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class OauthClientScope {

    @EmbeddedId
    private OauthClientScopePk oauthClientScopePk;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "clientId", insertable = false, updatable = false)
    private OauthClient oauthClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scopeId", insertable = false, updatable = false)
    private OauthScope oauthScope;
}
