package bssm.bsmauth.domain.oauth.presentation.dto;

import bssm.bsmauth.domain.user.domain.type.UserRole;
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
    private UserRole role;
    private String profileUrl;
}
