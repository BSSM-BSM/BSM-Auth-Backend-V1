package bssm.bsmauth.oauth;

import bssm.bsmauth.global.utils.UserUtil;
import bssm.bsmauth.oauth.dto.request.CreateOauthClientDto;
import bssm.bsmauth.oauth.dto.request.OauthAuthorizationDto;
import bssm.bsmauth.oauth.dto.response.OauthAuthenticationResponseDto;
import bssm.bsmauth.oauth.dto.response.OauthAuthorizationResponseDto;
import bssm.bsmauth.oauth.dto.response.OauthClientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OauthController {

    private final UserUtil userUtil;
    private final OauthService oauthService;

    @GetMapping("authentication")
    public OauthAuthenticationResponseDto authentication(
            @RequestParam String clientId,
            @RequestParam String redirectURI
    ) {
        return oauthService.authentication(userUtil.getCurrentUser(), clientId, redirectURI);
    }

    @PostMapping("authorization")
    public OauthAuthorizationResponseDto authorization(@RequestBody OauthAuthorizationDto dto) {
        return oauthService.authorization(userUtil.getCurrentUser(), dto);
    }

    @PostMapping("client")
    public void createClient(@RequestBody CreateOauthClientDto dto) {
        oauthService.createClient(userUtil.getCurrentUser(), dto);
    }

    @GetMapping("client")
    public List<OauthClientResponseDto> getClientList() {
        return oauthService.getClientList(userUtil.getCurrentUser());
    }
}
