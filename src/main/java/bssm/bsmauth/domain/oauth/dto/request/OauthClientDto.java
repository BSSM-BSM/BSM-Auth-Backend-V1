package bssm.bsmauth.domain.oauth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class OauthClientDto {

    private String clientId;
    private String clientSecret;
}
