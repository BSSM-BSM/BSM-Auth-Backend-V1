package bssm.bsmauth.oauth.entities;


import bssm.bsmauth.user.entities.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class OauthClient {

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

    @CreatedDate
    private Date createdAt;

    @OneToMany(mappedBy = "oauthClient", fetch = FetchType.EAGER)
    private List<OauthClientScope> scopes = new ArrayList<>();
}
