package bssm.bsmauth.domain.oauth.presentation.dto.request;

import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.type.OauthAccessType;
import bssm.bsmauth.domain.oauth.service.OauthScopeProvider;
import bssm.bsmauth.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static bssm.bsmauth.global.utils.Util.getRandomStr;

@Getter
@AllArgsConstructor
public class AddOauthClientRedirectRequest {

    @NotBlank
    private String clientId;

    @NotBlank
    @Size(
            min = 1,
            max = 100,
            message = "리다이렉트 URI는 1 ~ 100글자여야 합니다"
    )
    private String redirectUri;

    public OauthRedirectUri toEntity() {
        return OauthRedirectUri.builder().pk(
                OauthRedirectUriPk.builder()
                        .clientId(clientId)
                        .redirectUri(redirectUri)
                        .build()
        ).build();
    }
}
