package bssm.bsmauth.domain.oauth.presentation;

import bssm.bsmauth.domain.oauth.presentation.dto.response.*;
import bssm.bsmauth.domain.oauth.service.OauthService;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthAuthorizationRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthGetResourceRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthGetTokenRequest;
import bssm.bsmauth.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OauthController {

    private final CurrentUser currentUser;
    private final OauthService oauthService;

    @GetMapping("authenticate")
    public OauthAuthenticationResponseDto authentication(
            @RequestParam String clientId,
            @RequestParam String redirectURI
    ) {
        return oauthService.authentication(currentUser.getUser(), clientId, redirectURI);
    }

    @PostMapping("authorize")
    public OauthAuthorizationResponseDto authorization(@Valid @RequestBody OauthAuthorizationRequest dto) {
        return oauthService.authorization(currentUser.getUser(), dto);
    }

    @PostMapping("token")
    public OauthTokenResponseDto getToken(@RequestBody OauthGetTokenRequest dto) {
        return oauthService.getToken(dto);
    }

    @PostMapping("resource")
    public OauthResourceResponseDto getResource(@RequestBody OauthGetResourceRequest dto) {
        return oauthService.getResource(dto);
    }

}
