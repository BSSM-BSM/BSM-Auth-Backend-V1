package bssm.bsmauth.domain.oauth.presentation;

import bssm.bsmauth.domain.oauth.domain.OauthScope;
import bssm.bsmauth.domain.oauth.presentation.dto.request.*;
import bssm.bsmauth.domain.oauth.presentation.dto.response.*;
import bssm.bsmauth.domain.oauth.service.OauthManageService;
import bssm.bsmauth.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OauthManageController {

    private final CurrentUser currentUser;
    private final OauthManageService oauthManageService;

    @PostMapping("client")
    public void createClient(@Valid @RequestBody CreateOauthClientRequest dto) {
        oauthManageService.createClient(currentUser.findCachedUser(), dto);
    }

    @GetMapping("client")
    public List<OauthClientResponseDto> getClientList() {
        return oauthManageService.getClientList(currentUser.findCachedUser());
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
        oauthManageService.updateClient(currentUser.findCachedUser(), clientId, req);
    }

    @DeleteMapping("client/{clientId}")
    public void deleteClient(@PathVariable String clientId) {
        oauthManageService.deleteClient(currentUser.findCachedUser(), clientId);
    }

}
