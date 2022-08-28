package bssm.bsmauth.domain.oauth.dto.request;

import bssm.bsmauth.domain.oauth.type.OauthAccessType;
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
    private OauthAccessType access;
}
