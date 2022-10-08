package bssm.bsmauth.domain.oauth.dto.request;

import bssm.bsmauth.domain.oauth.type.OauthAccessType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateOauthClientRequest {

    @Size(min = 1, max = 63)
    @Pattern(
            regexp = "^([0-9]{1,3}.){3}[0-9]{1,3}|([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}?$",
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

    @NotBlank
    @Size(
            min = 1,
            max = 100,
            message = "는 1 ~ 100글자여야 합니다"
    )
    private String redirectURI;

    @Size(
            min = 1,
            message = "사용할 정보는 1개 이상이어야 합니다"
    )
    private String[] scopeList;

    @NotNull
    private OauthAccessType access;
}
