package bssm.bsmauth.domain.oauth.presentation;

import bssm.bsmauth.domain.oauth.domain.OauthScope;
import bssm.bsmauth.domain.oauth.presentation.dto.request.*;
import bssm.bsmauth.domain.oauth.presentation.dto.response.*;
import bssm.bsmauth.domain.oauth.service.OauthManageService;
import bssm.bsmauth.domain.oauth.service.OauthService;
import bssm.bsmauth.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OauthManageController {

    private final UserUtil userUtil;
    private final OauthManageService oauthManageService;

    @PostMapping("client")
    public void createClient(@Valid @RequestBody CreateOauthClientRequest dto) {
        oauthManageService.createClient(userUtil.getUser(), dto);
    }

    @GetMapping("client")
    public List<OauthClientResponseDto> getClientList() {
        return oauthManageService.getClientList(userUtil.getUser());
    }

    @GetMapping("scopes")
    public List<OauthScope> getScopeList() {
        return oauthManageService.getScopeList();
    }

    @PutMapping("client/{clientId}")
    public void updateClient(
            @PathVariable String clientId,
            @Valid @RequestBody UpdateOauthClientRequest req
    ) {
        oauthManageService.updateClient(userUtil.getUser(), clientId, req);
    }

    @DeleteMapping("client/{clientId}")
    public void deleteClient(@PathVariable String clientId) {
        oauthManageService.deleteClient(userUtil.getUser(), clientId);
    }

    @PostMapping("client/{clientId}/redirect")
    public void addRedirect(
            @PathVariable String clientId,
            @RequestPart(value = "redirectUri", required = false) String redirectUri
    ) {
        oauthManageService.addRedirectUri(userUtil.getUser(), new AddOauthClientRedirectRequest(clientId, redirectUri));
    }

    @DeleteMapping("client/{clientId}/redirect")
    public void removeRedirect(
            @PathVariable String clientId,
            @RequestParam String redirectUri
    ) {
        oauthManageService.removeRedirectUri(userUtil.getUser(), new RemoveOauthClientRedirectRequest(clientId, redirectUri));
    }
}
