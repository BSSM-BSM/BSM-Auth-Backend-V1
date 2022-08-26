package bssm.bsmauth.domain.oauth.dto.response;

import bssm.bsmauth.domain.oauth.dto.OauthUserDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OauthResourceResponseDto {

    private OauthUserDto user;
    private List<String> scopeList;
}
