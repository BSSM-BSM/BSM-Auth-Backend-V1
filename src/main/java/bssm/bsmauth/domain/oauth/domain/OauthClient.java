package bssm.bsmauth.domain.oauth.domain;


import bssm.bsmauth.domain.oauth.domain.type.OauthAccessType;
import bssm.bsmauth.domain.oauth.presentation.dto.response.OauthClientResponseDto;
import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.domain.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @Column(columnDefinition = "INT UNSIGNED")
    private Long userCode;

    @ManyToOne
    @JoinColumn(name = "userCode", insertable = false, updatable = false)
    private User user;

    @Column(length = 12, nullable = false)
    @Enumerated(EnumType.STRING)
    private OauthAccessType access;

    @OneToMany(mappedBy = "oauthClient", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<OauthClientScope> scopes = new HashSet<>();

    @OneToMany(mappedBy = "oauthClient", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<OauthRedirectUri> redirectUris = new HashSet<>();

    @OneToMany(mappedBy = "oauthClient", cascade = CascadeType.REMOVE)
    private final Set<OauthAuthCode> oauthAuthCodes = new HashSet<>();

    @OneToMany(mappedBy = "oauthClient", cascade = CascadeType.REMOVE)
    private final Set<OauthToken> oauthTokens = new HashSet<>();

    @Builder
    public OauthClient(String id, String clientSecret, String domain, String serviceName, Long userCode, User user, OauthAccessType access, Set<OauthClientScope> scopes, Set<OauthRedirectUri> redirectUris) {
        this.id = id;
        this.clientSecret = clientSecret;
        this.domain = domain;
        this.serviceName = serviceName;
        this.userCode = userCode;
        this.user = user;
        this.access = access;
        this.scopes = scopes;
        this.redirectUris = redirectUris;
    }

    public OauthClientResponseDto toResponse() {
        return OauthClientResponseDto.builder()
                .clientId(id)
                .clientSecret(clientSecret)
                .domain(domain)
                .serviceName(serviceName)
                .redirectUriList(redirectUris.stream()
                        .map(OauthRedirectUri::toUriString)
                        .toList())
                .scopeList(scopes.stream()
                        .map(scope -> scope.getOauthScope().getId())
                        .toList())
                .access(access)
                .build();
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setAccess(OauthAccessType access) {
        this.access = access;
    }
}
