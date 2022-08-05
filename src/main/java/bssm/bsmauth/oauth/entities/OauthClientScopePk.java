package bssm.bsmauth.oauth.entities;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Embeddable
public class OauthClientScopePk implements Serializable {

    @Column(length = 8)
    private String clientId;

    @Column(length = 16)
    private String scopeId;
}
