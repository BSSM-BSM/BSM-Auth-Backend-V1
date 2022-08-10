package bssm.bsmauth.oauth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class OauthScope {

    @Id
    @Column(length = 16)
    private String id;

    @JsonIgnore
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private int idx;

    @Column(nullable = false, length = 16)
    private String name;

    @Column(nullable = false, length = 100)
    private String description;
}
