package bssm.bsmauth.oauth.dto.response;

import bssm.bsmauth.oauth.dto.OauthUserDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OauthResourceResponseDto {

    private OauthUserDto user;
    private List<String> scopeList;
}
