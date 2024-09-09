package bssm.bsmauth.domain.oauth.presentation;

import bssm.bsmauth.domain.oauth.presentation.dto.res.*;
import bssm.bsmauth.domain.oauth.service.OauthService;
import bssm.bsmauth.domain.oauth.presentation.dto.req.OauthAuthorizationReq;
import bssm.bsmauth.domain.oauth.presentation.dto.req.OauthGetResourceReq;
import bssm.bsmauth.domain.oauth.presentation.dto.req.OauthGetTokenReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("authenticate")
    public OauthAuthenticationRes authentication(
            @RequestParam String clientId,
            @RequestParam String redirectURI
    ) {
        return oauthService.authentication(clientId, redirectURI);
    }

    @PostMapping("authorize")
    public OauthAuthorizationRes authorization(@Valid @RequestBody OauthAuthorizationReq req) {
        return oauthService.authorization(req);
    }

    @PostMapping("token")
    public OauthTokenRes getToken(@RequestBody OauthGetTokenReq req) {
        return oauthService.getToken(req);
    }

    @PostMapping("resource")
    public OauthResourceRes getResource(@RequestBody OauthGetResourceReq req) {
        return oauthService.getResource(req);
    }

}
