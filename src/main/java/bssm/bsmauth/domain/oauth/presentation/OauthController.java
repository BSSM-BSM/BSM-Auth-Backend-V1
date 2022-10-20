package bssm.bsmauth.domain.oauth.presentation;

import bssm.bsmauth.domain.oauth.presentation.dto.response.*;
import bssm.bsmauth.domain.oauth.service.OauthService;
import bssm.bsmauth.global.utils.UserUtil;
import bssm.bsmauth.domain.oauth.presentation.dto.request.CreateOauthClientRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthAuthorizationRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthGetResourceRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthGetTokenRequest;
import bssm.bsmauth.domain.oauth.domain.OauthScope;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OauthController {

    private final UserUtil userUtil;
    private final OauthService oauthService;

    @GetMapping("authenticate")
    public OauthAuthenticationResponseDto authentication(
            @RequestParam String clientId,
            @RequestParam String redirectURI
    ) {
        return oauthService.authentication(userUtil.getUser(), clientId, redirectURI);
    }

    @PostMapping("authorize")
    public OauthAuthorizationResponseDto authorization(@Valid @RequestBody OauthAuthorizationRequest dto) {
        return oauthService.authorization(userUtil.getUser(), dto);
    }

    @PostMapping("token")
    public OauthTokenResponseDto getToken(@RequestBody OauthGetTokenRequest dto) {
        return oauthService.getToken(dto);
    }

    @PostMapping("resource")
    public OauthResourceResponseDto getResource(@RequestBody OauthGetResourceRequest dto) {
        return oauthService.getResource(dto);
    }

    @PostMapping("client")
    public void createClient(@Valid @RequestBody CreateOauthClientRequest dto) {
        oauthService.createClient(userUtil.getUser(), dto);
    }

    @GetMapping("client")
    public List<OauthClientResponseDto> getClientList() {
        return oauthService.getClientList(userUtil.getUser());
    }

    @GetMapping("scopes")
    public List<OauthScope> getScopeList() {
        return oauthService.getScopeList();
    }

    @DeleteMapping("client/{clientId}")
    public void deleteClient(@PathVariable String clientId) {
        oauthService.deleteClient(userUtil.getUser(), clientId);
    }
}
