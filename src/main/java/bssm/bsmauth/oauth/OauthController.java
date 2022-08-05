package bssm.bsmauth.oauth;

import bssm.bsmauth.global.utils.UserUtil;
import bssm.bsmauth.oauth.dto.request.CreateOauthClientDto;
import bssm.bsmauth.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class OauthController {

    private final UserUtil userUtil;
    private final OauthService oauthService;

    @PostMapping("client")
    public void createClient(@RequestBody CreateOauthClientDto dto) {
        oauthService.createClient(userUtil.getCurrentUser(), dto);
    }
}
