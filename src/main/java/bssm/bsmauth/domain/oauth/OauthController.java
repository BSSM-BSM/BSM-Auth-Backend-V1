package bssm.bsmauth.domain.oauth;

import bssm.bsmauth.domain.oauth.dto.response.*;
import bssm.bsmauth.global.utils.UserUtil;
import bssm.bsmauth.domain.oauth.dto.request.CreateOauthClientRequest;
import bssm.bsmauth.domain.oauth.dto.request.OauthAuthorizationRequest;
import bssm.bsmauth.domain.oauth.dto.request.OauthGetResourceRequest;
import bssm.bsmauth.domain.oauth.dto.request.OauthGetTokenRequest;
import bssm.bsmauth.domain.oauth.entities.OauthScope;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        return oauthService.authentication(userUtil.getCurrentUser(), clientId, redirectURI);
    }

    @PostMapping("authorize")
    public OauthAuthorizationResponseDto authorization(@RequestBody OauthAuthorizationRequest dto) {
        return oauthService.authorization(userUtil.getCurrentUser(), dto);
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
    public void createClient(@RequestBody CreateOauthClientRequest dto) {
        oauthService.createClient(userUtil.getCurrentUser(), dto);
    }

    @GetMapping("client")
    public List<OauthClientResponseDto> getClientList() {
        return oauthService.getClientList(userUtil.getCurrentUser());
    }

    @GetMapping("scopes")
    public List<OauthScope> getScopeList() {
        return oauthService.getScopeList();
    }

    @DeleteMapping("client/{clientId}")
    public void deleteClient(@PathVariable String clientId) {
        oauthService.deleteClient(userUtil.getCurrentUser(), clientId);
    }
}
