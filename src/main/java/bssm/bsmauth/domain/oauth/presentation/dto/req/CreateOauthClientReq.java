package bssm.bsmauth.domain.oauth.presentation.dto.req;

import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.type.OauthAccessType;
import bssm.bsmauth.domain.oauth.service.OauthScopeProvider;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.utils.SecurityUtil;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CreateOauthClientReq {

    @Size(min = 1, max = 63)
    @Pattern(
            regexp = "^([0-9]{1,3}.){3}[0-9]{1,3}|([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}?$|^localhost$",
            message = "도메인이 잘못되었습니다"
    )
    private String domain;

    @NotBlank
    @Size(
            min = 2,
            max = 32,
            message = "서비스 이름은 2 ~ 32글자여야 합니다"
    )
    private String serviceName;

    @Size(
            min = 1,
            max = 10,
            message = "리다이렉트 URI의 갯수는 1 ~ 10개여야 합니다"
    )
    private List<
            @NotBlank
            @Size(
                    min = 1,
                    max = 100,
                    message = "리다이렉트 URI는 1 ~ 100글자여야 합니다"
            )
            String
    > redirectUriList;

    @Size(
            min = 1,
            message = "사용할 정보는 1개 이상이어야 합니다"
    )
    private List<String> scopeList;

    @NotNull
    private OauthAccessType access;

    public OauthClient toEntity(User user) {
        String clientId = SecurityUtil.getRandomString(8);

        return OauthClient.builder()
                .id(clientId)
                .clientSecret(SecurityUtil.getRandomString(32))
                .domain(domain)
                .serviceName(serviceName)
                .userCode(user.getCode())
                .access(access)
                .build();
    }

    public Set<OauthRedirectUri> toRedirectEntitySet(String clientId) {
        return redirectUriList.stream()
                .map(redirectUri -> toRedirectEntity(clientId, redirectUri))
                .collect(Collectors.toSet());
    }

    private OauthRedirectUri toRedirectEntity(String clientId, String redirectUri) {
        return OauthRedirectUri.builder().pk(
                OauthRedirectUriPk.builder()
                        .clientId(clientId)
                        .redirectUri(redirectUri)
                        .build()
        ).build();
    }

    public Set<OauthClientScope> toScopeEntitySet(String clientId, OauthScopeProvider oauthScopeProvider) {
        return scopeList.stream()
                .map(scope -> toScopeEntity(clientId, scope, oauthScopeProvider))
                .collect(Collectors.toSet());
    }

    public OauthClientScope toScopeEntity(String clientId, String scope, OauthScopeProvider oauthScopeProvider) {
        return OauthClientScope.builder()
                .pk(
                        new OauthClientScopePk(clientId, oauthScopeProvider.getScope(scope).getId())
                ).build();
    }
}
