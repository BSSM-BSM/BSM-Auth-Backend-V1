package bssm.bsmauth.domain.oauth.service;

import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.repository.*;
import bssm.bsmauth.domain.oauth.facade.OauthFacade;
import bssm.bsmauth.domain.oauth.presentation.dto.response.*;
import bssm.bsmauth.global.error.exceptions.BadRequestException;
import bssm.bsmauth.global.error.exceptions.InternalServerException;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import bssm.bsmauth.domain.oauth.presentation.dto.OauthUserDto;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthAuthorizationRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthGetResourceRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthGetTokenRequest;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.repository.UserRepository;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static bssm.bsmauth.global.utils.Util.getRandomStr;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthAuthCodeRepository oauthAuthCodeRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final UserRepository userRepository;
    private final OauthScopeProvider oauthScopeProvider;
    private final OauthFacade oauthFacade;

    public OauthAuthenticationResponseDto authentication(User user, String clientId, String redirectURI) {
        OauthClient client = oauthFacade.checkClient(user, clientId, redirectURI);

        // 이미 인증이 되었다면
        if (oauthTokenRepository.findByUserCodeAndClientId(user.getCode(), clientId).isPresent()) {
            return OauthAuthenticationResponseDto.builder()
                    .authorized(true)
                    .domain(client.getDomain())
                    .serviceName(client.getServiceName())
                    .build();
        }

        List<OauthScope> scopeList = new ArrayList<>();
        client.getScopes()
                .forEach(scope -> scopeList.add(oauthScopeProvider.getScope(scope.getOauthScope().getId())));

        return OauthAuthenticationResponseDto.builder()
                .authorized(false)
                .domain(client.getDomain())
                .serviceName(client.getServiceName())
                .scopeList(scopeList)
                .build();
    }

    public OauthAuthorizationResponseDto authorization(User user, OauthAuthorizationRequest dto) {
        OauthClient client = oauthFacade.checkClient(user, dto.getClientId(), dto.getRedirectURI());

        OauthAuthCode authCode = OauthAuthCode.builder()
                .code(getRandomStr(32))
                .userCode(user.getCode())
                .oauthClient(client)
                .build();
        oauthAuthCodeRepository.save(authCode);

        return OauthAuthorizationResponseDto.builder()
                .redirectURI(dto.getRedirectURI() + "?code=" + authCode.getCode())
                .build();
    }

    public OauthTokenResponseDto getToken(OauthGetTokenRequest dto) {
        OauthAuthCode authCode = oauthAuthCodeRepository.findByCodeAndExpire(dto.getAuthCode(), false).orElseThrow(
                () -> {throw new NotFoundException("인증 코드를 찾을 수 없습니다");}
        );
        OauthClient client = authCode.getOauthClient();
        if ( !(client.getId().equals(dto.getClientId()) && client.getClientSecret().equals(dto.getClientSecret())) ) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("client", "클라이언트 정보가 잘못되었습니다").
                    build()
            );
        }

        authCode.setExpire(true);
        oauthAuthCodeRepository.save(authCode);

        Optional<OauthToken> token = oauthTokenRepository.findByUserCodeAndClientId(authCode.getUserCode(), dto.getClientId());
        if (token.isPresent()) {
            return OauthTokenResponseDto.builder()
                    .token(token.get().getToken())
                    .build();
        }

        OauthToken newToken = OauthToken.builder()
                .token(getRandomStr(32))
                .userCode(authCode.getUserCode())
                .oauthClient(client)
                .build();
        oauthTokenRepository.save(newToken);

        return OauthTokenResponseDto.builder()
                .token(newToken.getToken())
                .build();
    }

    public OauthResourceResponseDto getResource(OauthGetResourceRequest dto) {
        OauthToken token = oauthTokenRepository.findByTokenAndExpire(dto.getToken(), false)
                .orElseThrow(() -> new NotFoundException("토큰을 찾을 수 없습니다"));
        OauthClient client = token.getOauthClient();
        if ( !(client.getId().equals(dto.getClientId()) && client.getClientSecret().equals(dto.getClientSecret())) ) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("client", "클라이언트 정보가 잘못되었습니다").
                    build()
            );
        }

        User user = userRepository.findById(token.getUserCode())
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다"));

        List<String> scopeList = new ArrayList<>();
        client.getScopes()
                .forEach(scope -> scopeList.add(scope.getOauthScope().getId()));

        OauthUserDto.OauthUserDtoBuilder oauthUserDto = OauthUserDto.builder();
        scopeList.forEach(scope -> {
            switch (user.getRole()) {
                case STUDENT -> {
                    switch (scope) {
                        case "code" -> oauthUserDto.code(user.getCode());
                        case "nickname" -> oauthUserDto.nickname(user.getNickname());
                        case "enrolledAt" -> oauthUserDto.enrolledAt(user.getStudent().getEnrolledAt());
                        case "grade" -> oauthUserDto.grade(user.getStudent().getGrade());
                        case "classNo" -> oauthUserDto.classNo(user.getStudent().getClassNo());
                        case "studentNo" -> oauthUserDto.studentNo(user.getStudent().getStudentNo());
                        case "name" -> oauthUserDto.name(user.getStudent().getName());
                        case "email" -> oauthUserDto.email(user.getStudent().getEmail());
                    }
                }
                case TEACHER -> {
                    switch (scope) {
                        case "code" -> oauthUserDto.code(user.getCode());
                        case "nickname" -> oauthUserDto.nickname(user.getNickname());
                        case "name" -> oauthUserDto.name(user.getTeacher().getName());
                        case "email" -> oauthUserDto.email(user.getTeacher().getEmail());
                    }
                }
                default -> throw new InternalServerException();
            }
        });

        return OauthResourceResponseDto.builder()
                .scopeList(scopeList)
                .user(oauthUserDto
                        .role(user.getRole())
                        .build()
                )
                .build();
    }
}
