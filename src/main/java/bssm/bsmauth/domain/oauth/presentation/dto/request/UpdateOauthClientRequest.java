package bssm.bsmauth.domain.oauth.presentation.dto.request;

import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.type.OauthAccessType;
import bssm.bsmauth.domain.oauth.service.OauthScopeProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UpdateOauthClientRequest {

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
            message = "사용할 정보는 1개 이상이어야 합니다"
    )
    private List<String> scopeList;

    @NotNull
    private OauthAccessType access;

    @Transactional
    public void updateClient(OauthClient client) {
        client.setDomain(domain);
        client.setServiceName(serviceName);
        client.setAccess(access);
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
