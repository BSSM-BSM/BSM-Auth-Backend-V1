package bssm.bsmauth.domain.oauth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public OauthScope(String id, int idx, String name, String description) {
        this.id = id;
        this.idx = idx;
        this.name = name;
        this.description = description;
    }
}
