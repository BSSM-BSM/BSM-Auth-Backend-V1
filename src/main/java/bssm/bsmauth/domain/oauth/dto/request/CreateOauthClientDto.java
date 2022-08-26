package bssm.bsmauth.domain.oauth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateOauthClientDto {

    private String domain;
    private String serviceName;
    private String redirectURI;
    private String[] scopeList;
}
