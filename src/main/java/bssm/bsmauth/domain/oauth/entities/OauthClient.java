package bssm.bsmauth.domain.oauth.entities;


import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.domain.user.entities.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthClient extends BaseTimeEntity {

    @Id
    @Column(length = 8)
    private String id;

    @Column(nullable = false, length = 32)
    private String clientSecret;

    @Column(nullable = false, length = 63)
    private String domain;

    @Column(nullable = false, length = 32)
    private String serviceName;

    @Column(nullable = false, length = 100)
    private String redirectURI;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long usercode;

    @ManyToOne
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "oauthClient", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<OauthClientScope> scopes = new ArrayList<>();

    @Builder
    public OauthClient(String id, String clientSecret, String domain, String serviceName, String redirectURI, Long usercode, User user, List<OauthClientScope> scopes) {
        this.id = id;
        this.clientSecret = clientSecret;
        this.domain = domain;
        this.serviceName = serviceName;
        this.redirectURI = redirectURI;
        this.usercode = usercode;
        this.user = user;
        this.scopes = scopes;
    }
}
