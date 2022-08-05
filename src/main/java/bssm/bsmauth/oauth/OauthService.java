package bssm.bsmauth.oauth;

import bssm.bsmauth.global.exceptions.BadRequestException;
import bssm.bsmauth.oauth.dto.request.CreateOauthClientDto;
import bssm.bsmauth.oauth.entities.OauthClient;
import bssm.bsmauth.oauth.entities.OauthClientScope;
import bssm.bsmauth.oauth.entities.OauthClientScopePk;
import bssm.bsmauth.oauth.repositories.OauthClientRepository;
import bssm.bsmauth.oauth.repositories.OauthClientScopeRepository;
import bssm.bsmauth.oauth.utils.OauthScopeUtil;
import bssm.bsmauth.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthClientRepository oauthClientRepository;
    private final OauthClientScopeRepository oauthClientScopeRepository;
    private final OauthScopeUtil oauthScopeUtil;

    public void createClient(User user, CreateOauthClientDto dto) {
        if (!domainCheck(dto.getDomain())) throw new BadRequestException("도메인이 잘못되었습니다");
        if (!uriCheck(dto.getDomain(), dto.getRedirectURI())) throw new BadRequestException("리다이렉트 주소가 잘못되었습니다");
        System.out.println(getRandomStr(32));

        OauthClient client = OauthClient.builder()
                .id(getRandomStr(8))
                .clientSecret(getRandomStr(32))
                .domain(dto.getDomain())
                .serviceName(dto.getServiceName())
                .redirectURI(dto.getRedirectURI())
                .usercode(user.getUsercode())
                .build();

        List<OauthClientScope> clientScopeList = new ArrayList<>();
        for (String scope : dto.getScopeList()) {
            clientScopeList.add(
                    OauthClientScope.builder()
                            .oauthClientScopePk(new OauthClientScopePk(client.getId(), oauthScopeUtil.getScope(scope).getId()))
                            .build()
            );
        }

        oauthClientRepository.save(client);
        oauthClientScopeRepository.saveAll(clientScopeList);
    }

    private String getRandomStr(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length / 2];
        secureRandom.nextBytes(randomBytes);
        return HexFormat.of().formatHex(randomBytes);
    }


    private boolean domainCheck(String str) {
        if (str.equals("localhost")) return true;
        return Pattern.matches("^([0-9]{1,3}.){3}[0-9]{1,3}|([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}?$", str);
    }

    private boolean uriCheck (String domain, String uri) {
        return Pattern.matches("(https?://)("+domain+")(:(6[0-5]{2}[0-3][0-5]|[1-5][0-9]{4}|[1-9][0-9]{0,3}))?/.*", uri);
    }
}
