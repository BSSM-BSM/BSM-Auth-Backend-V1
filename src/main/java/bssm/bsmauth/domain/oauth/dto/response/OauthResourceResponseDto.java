package bssm.bsmauth.domain.oauth.dto.response;

import bssm.bsmauth.domain.oauth.dto.OauthUserDto;
import bssm.bsmauth.domain.oauth.type.OauthAccessType;
import bssm.bsmauth.domain.user.type.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OauthResourceResponseDto {

    private OauthUserDto user;
    private List<String> scopeList;
    private UserRole role;
}
