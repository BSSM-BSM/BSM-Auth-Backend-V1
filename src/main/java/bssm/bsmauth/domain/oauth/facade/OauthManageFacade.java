package bssm.bsmauth.domain.oauth.facade;

import bssm.bsmauth.domain.oauth.domain.OauthClient;
import bssm.bsmauth.domain.oauth.domain.repository.OauthClientRepository;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.error.exceptions.BadRequestException;
import bssm.bsmauth.global.error.exceptions.ForbiddenException;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class OauthManageFacade {

    private final OauthClientRepository oauthClientRepository;

    public OauthClient findById(String clientId) {
        return oauthClientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("클라이언트를 찾을 수 없습니다"));
    }

    public List<OauthClient> findAllByUser(User user) {
        return oauthClientRepository.findAllByUserCode(user.getCode());
    }

    public OauthClient save(OauthClient client) {
        return oauthClientRepository.save(client);
    }

    public void delete(OauthClient client) {
        oauthClientRepository.delete(client);
    }

    public void permissionCheck(OauthClient client, User user) {
        if (!client.getUserCode().equals(user.getCode())) {
            throw new ForbiddenException("권한이 없습니다");
        }
    }

    public void uriCheck(String domain, String uri) {
        if (!Pattern.matches("(https?://)("+domain+"|localhost)(:(6[0-5]{2}[0-3][0-5]|[1-5][0-9]{4}|[1-9][0-9]{0,3}))?/.*", uri)) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("redirectURI", "리다이렉트 주소가 올바르지 않습니다").
                    build()
            );
        }
    }

}
