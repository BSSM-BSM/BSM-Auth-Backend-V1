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
import bssm.bsmauth.global.utils.SecurityUtil;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        OauthAuthCode authCode = OauthAuthCode.builder()
                .code(SecurityUtil.getRandomString(32))
                .user(user)
                .oauthClient(client)
                .build();
        oauthAuthCodeRepository.save(authCode);

        return OauthAuthorizationRes.builder()
                .redirectURI(req.getRedirectURI() + "?code=" + authCode.getCode())
                .build();
    }

    public OauthTokenRes getToken(OauthGetTokenReq req) {
        OauthAuthCode authCode = oauthAuthCodeRepository.findByCodeAndExpire(req.getAuthCode(), false)
                .orElseThrow(() -> new NotFoundException("인증 코드를 찾을 수 없습니다"));
        OauthClient client = authCode.getOauthClient();
        if ( !(client.getId().equals(req.getClientId()) && client.getClientSecret().equals(req.getClientSecret())) ) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("client", "클라이언트 정보가 잘못되었습니다").
                    build()
            );
        }

        authCode.expire();
        oauthAuthCodeRepository.save(authCode);

        Optional<OauthToken> token = oauthTokenRepository.findByUserAndOauthClient(authCode.getUser(), client);
        if (token.isPresent()) {
            return OauthTokenRes.builder()
                    .token(token.get().getToken())
                    .build();
        }

        OauthToken newToken = OauthToken.builder()
                .token(SecurityUtil.getRandomString(32))
                .user(authCode.getUser())
                .oauthClient(client)
                .build();
        oauthTokenRepository.save(newToken);

        return OauthTokenRes.builder()
                .token(newToken.getToken())
                .build();
    }

    public OauthResourceRes getResource(OauthGetResourceReq req) {
        OauthToken token = oauthTokenRepository.findByTokenAndIsExpired(req.getToken(), false)
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
