package bssm.bsmauth.oauth;

import bssm.bsmauth.global.utils.UserUtil;
import bssm.bsmauth.oauth.dto.request.CreateOauthClientDto;
import bssm.bsmauth.oauth.dto.request.OauthAuthorizationDto;
import bssm.bsmauth.oauth.dto.request.OauthGetResourceDto;
import bssm.bsmauth.oauth.dto.request.OauthGetTokenDto;
import bssm.bsmauth.oauth.dto.response.*;
import bssm.bsmauth.oauth.entities.OauthScope;
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
    public OauthAuthorizationResponseDto authorization(@RequestBody OauthAuthorizationDto dto) {
        return oauthService.authorization(userUtil.getCurrentUser(), dto);
    }

    @PostMapping("token")
    public OauthTokenResponseDto getToken(@RequestBody OauthGetTokenDto dto) {
        return oauthService.getToken(dto);
    }

    @PostMapping("resource")
    public OauthResourceResponseDto getResource(@RequestBody OauthGetResourceDto dto) {
        return oauthService.getResource(dto);
    }

    @PostMapping("client")
    public void createClient(@RequestBody CreateOauthClientDto dto) {
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
}
