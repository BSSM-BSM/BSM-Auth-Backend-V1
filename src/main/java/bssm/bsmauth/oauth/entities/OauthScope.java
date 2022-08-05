package bssm.bsmauth.oauth.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class OauthScope {

    @Id
    @Column(length = 16)
    private String id;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private int idx;

    @Column(nullable = false, length = 16)
    private String name;

    @Column(nullable = false, length = 100)
    private String description;
}
