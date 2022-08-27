package bssm.bsmauth.domain.oauth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OauthUserDto {

    private Long code;
    private String nickname;
    private Integer enrolledAt;
    private Integer grade;
    private Integer classNo;
    private Integer studentNo;
    private String name;
    private String email;
}
