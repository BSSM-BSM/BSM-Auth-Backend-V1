package bssm.bsmauth.domain.oauth.facade;

import bssm.bsmauth.domain.oauth.domain.type.OauthAccessType;
import bssm.bsmauth.domain.oauth.domain.OauthClient;
import bssm.bsmauth.domain.oauth.domain.OauthRedirectUri;
import bssm.bsmauth.domain.oauth.domain.repository.OauthClientRepository;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.error.exceptions.BadRequestException;
import bssm.bsmauth.global.error.exceptions.ForbiddenException;
import bssm.bsmauth.global.error.exceptions.InternalServerException;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OauthFacade {

    private final OauthClientRepository oauthClientRepository;

    public OauthClient findById(String clientId) {
        return oauthClientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("클라이언트를 찾을 수 없습니다"));
    }

    public OauthClient checkClient(User user, String clientId, String redirectURI) {
        OauthClient client = findById(clientId);

        checkUri(client, redirectURI);
        checkAccessible(client, user);

        return client;
    }

    private void checkAccessible(OauthClient client, User user) {
        if (client.getAccess() == OauthAccessType.ALL) return;
        if (client.getAccess() == OauthAccessType.valueOf(user.getRole().name())) return;

        String msg;
        switch (client.getAccess()) {
            case STUDENT -> msg = "해당 서비스는 학생만 사용할 수 있습니다";
            case TEACHER -> msg = "해당 서비스는 선생님만 사용할 수 있습니다";
            default -> throw new InternalServerException();
        }
        throw new ForbiddenException(msg);
    }

    private void checkUri(OauthClient client, String redirectURI) {
        List<String> uris = client.getRedirectUris()
                .stream().map(OauthRedirectUri::toUriString)
                .toList();

        if (!uris.contains(redirectURI)) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("redirectURI", "리다이렉트 주소가 올바르지 않습니다").
                    build()
            );
        }
    }

}
