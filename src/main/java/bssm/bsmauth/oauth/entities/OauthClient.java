package bssm.bsmauth.oauth.entities;


import bssm.bsmauth.global.entity.BaseTimeEntity;
import bssm.bsmauth.user.entities.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
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
    private int usercode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usercode", insertable = false, updatable = false)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "oauthClient", fetch = FetchType.EAGER)
    private List<OauthClientScope> scopes = new ArrayList<>();
}
