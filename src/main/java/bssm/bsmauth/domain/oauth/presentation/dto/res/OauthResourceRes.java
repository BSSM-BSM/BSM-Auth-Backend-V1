package bssm.bsmauth.domain.oauth.presentation.dto.res;

import bssm.bsmauth.domain.oauth.presentation.dto.OauthUserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OauthResourceRes {

    private OauthUserDto user;
    private List<String> scopeList;

    public static OauthResourceRes create(OauthUserDto user, List<String> scopeList) {
        OauthResourceRes res = new OauthResourceRes();
        res.user = user;
        res.scopeList = scopeList;
        return res;
    }
}
