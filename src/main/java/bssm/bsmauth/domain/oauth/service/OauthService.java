package bssm.bsmauth.domain.oauth.service;

import bssm.bsmauth.domain.auth.exception.NoSuchTokenException;
import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.repository.*;
import bssm.bsmauth.domain.oauth.facade.OauthFacade;
import bssm.bsmauth.domain.oauth.presentation.dto.res.*;
import bssm.bsmauth.domain.user.exception.NoSuchUserException;
import bssm.bsmauth.global.auth.CurrentUser;
import bssm.bsmauth.global.error.exceptions.BadRequestException;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import bssm.bsmauth.domain.oauth.presentation.dto.OauthUserDto;
import bssm.bsmauth.domain.oauth.presentation.dto.req.OauthAuthorizationReq;
import bssm.bsmauth.domain.oauth.presentation.dto.req.OauthGetResourceReq;
import bssm.bsmauth.domain.oauth.presentation.dto.req.OauthGetTokenReq;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.repository.UserRepository;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final CurrentUser currentUser;
    private final OauthFacade oauthFacade;
    private final OauthScopeProvider oauthScopeProvider;

    private final OauthAuthCodeRepository oauthAuthCodeRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final UserRepository userRepository;

    public OauthAuthenticationRes authentication(String clientId, String redirectURI) {
        User user = currentUser.findCachedUser();
        OauthClient client = oauthFacade.checkClient(user, clientId, redirectURI);

        // 이미 인증이 되었다면
        if (oauthTokenRepository.findByUserAndOauthClient(user, client).isPresent()) {
            return OauthAuthenticationRes.builder()
                    .authorized(true)
                    .domain(client.getDomain())
                    .serviceName(client.getServiceName())
                    .build();
        }

        List<OauthScope> scopeList = new ArrayList<>();
        client.getScopes()
                .forEach(scope -> scopeList.add(oauthScopeProvider.getScope(scope.getOauthScope().getId())));

        return OauthAuthenticationRes.builder()
                .authorized(false)
                .domain(client.getDomain())
                .serviceName(client.getServiceName())
                .scopeList(scopeList)
                .build();
    }

    public OauthAuthorizationRes authorization(OauthAuthorizationReq req) {
        User user = currentUser.findUser();
        OauthClient client = oauthFacade.checkClient(user, req.getClientId(), req.getRedirectURI());

        OauthAuthCode authCode = OauthAuthCode.create(client, user);
        oauthAuthCodeRepository.save(authCode);

        return OauthAuthorizationRes.builder()
                .redirectURI(req.getRedirectURI() + "?code=" + authCode.getCode())
                .build();
    }

    public OauthTokenRes getToken(OauthGetTokenReq req) {
        OauthAuthCode authCode = oauthAuthCodeRepository.findByCode(req.getAuthCode())
                .orElseThrow(() -> new NotFoundException("인증 코드를 찾을 수 없습니다"));
        LocalDateTime expireIn = authCode.getCreatedAt().plusMinutes(5);
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expireIn)) {
            throw new NotFoundException("인증 코드를 찾을 수 없습니다");
        }

        OauthClient client = authCode.getOauthClient();
        if ( !(client.getId().equals(req.getClientId()) && client.getClientSecret().equals(req.getClientSecret())) ) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("client", "클라이언트 정보가 잘못되었습니다").
                    build()
            );
        }

        authCode.use();
        oauthAuthCodeRepository.save(authCode);

        OauthToken token = oauthTokenRepository.findByUserAndOauthClient(authCode.getUser(), client)
                .orElseGet(() -> {
                    OauthToken newToken = OauthToken.create(client, authCode.getUser());
                    return oauthTokenRepository.save(newToken);
                });

        return OauthTokenRes.builder()
                .token(token.getToken())
                .build();
    }

    public OauthResourceRes getResource(OauthGetResourceReq req) {
        OauthToken token = oauthTokenRepository.findByTokenAndIsActive(req.getToken(), true)
                .orElseThrow(NoSuchTokenException::new);
        OauthClient client = token.getOauthClient();
        if ( !(client.getId().equals(req.getClientId()) && client.getClientSecret().equals(req.getClientSecret())) ) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("client", "클라이언트 정보가 잘못되었습니다").
                    build()
            );
        }

        User user = userRepository.findById(token.getUser().getId())
                .orElseThrow(NoSuchUserException::new);

        List<OauthScope> scopeList = client.getScopes().stream()
                .map(OauthClientScope::getOauthScope)
                .toList();

        OauthUserDto oauthUserDto = OauthUserDto.create(scopeList, user);
        return OauthResourceRes.create(oauthUserDto, scopeList);
    }
}
