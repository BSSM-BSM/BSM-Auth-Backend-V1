package bssm.bsmauth.domain.auth.presentation.dto.res;

import lombok.Getter;

@Getter
public class AuthTokenRes {

    private String accessToken;
    private String refreshToken;

    public static AuthTokenRes create(String accessToken, String refreshToken) {
        AuthTokenRes res = new AuthTokenRes();
        res.accessToken = accessToken;
        res.refreshToken = refreshToken;
        return res;
    }
}
