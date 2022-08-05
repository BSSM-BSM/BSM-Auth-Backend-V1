package bssm.bsmauth.oauth;

import bssm.bsmauth.global.utils.UserUtil;
import bssm.bsmauth.oauth.dto.request.CreateOauthClientDto;
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

    @PostMapping("client")
    public void createClient(@RequestBody CreateOauthClientDto dto) {
        oauthService.createClient(userUtil.getCurrentUser(), dto);
    }

    @GetMapping("client")
    public List<OauthClientResponseDto> getClientList() {
        return oauthService.getClientList(userUtil.getCurrentUser());
    }
}
