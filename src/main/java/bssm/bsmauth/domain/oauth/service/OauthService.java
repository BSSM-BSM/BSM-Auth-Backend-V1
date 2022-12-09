package bssm.bsmauth.domain.oauth.service;

import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.OauthAccessType;
import bssm.bsmauth.domain.oauth.domain.repository.*;
import bssm.bsmauth.domain.oauth.presentation.dto.response.*;
import bssm.bsmauth.global.error.exceptions.BadRequestException;
import bssm.bsmauth.global.error.exceptions.ForbiddenException;
import bssm.bsmauth.global.error.exceptions.InternalServerException;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import bssm.bsmauth.domain.oauth.presentation.dto.OauthUserDto;
import bssm.bsmauth.domain.oauth.presentation.dto.request.CreateOauthClientRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthAuthorizationRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthGetResourceRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.OauthGetTokenRequest;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.repository.UserRepository;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static bssm.bsmauth.global.utils.Util.getRandomStr;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthClientRepository oauthClientRepository;
    private final OauthClientScopeRepository oauthClientScopeRepository;
    private final OauthRedirectUriRepository oauthRedirectUriRepository;
    private final OauthAuthCodeRepository oauthAuthCodeRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final UserRepository userRepository;
    private final OauthScopeProvider oauthScopeProvider;
    private final OauthFacade oauthFacade;

    public OauthAuthenticationResponseDto authentication(User user, String clientId, String redirectURI) {
        OauthClient client = checkClient(user, clientId, redirectURI);

        // 이미 인증이 되었다면
        if (oauthTokenRepository.findByUserCodeAndClientId(user.getCode(), clientId).isPresent()) {
            return OauthAuthenticationResponseDto.builder()
                    .authorized(true)
                    .domain(client.getDomain())
                    .serviceName(client.getServiceName())
                    .build();
        }

        List<OauthScope> scopeList = new ArrayList<>();
        client.getScopes().forEach(scope -> {
            scopeList.add(oauthScopeProvider.getScope(scope.getOauthScope().getId()));
        });

        return OauthAuthenticationResponseDto.builder()
                .authorized(false)
                .domain(client.getDomain())
                .serviceName(client.getServiceName())
                .scopeList(scopeList)
                .build();
    }

    public OauthAuthorizationResponseDto authorization(User user, OauthAuthorizationRequest dto) {
        OauthClient client = checkClient(user, dto.getClientId(), dto.getRedirectURI());

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

    private OauthClient checkClient(User user, String clientId, String redirectURI) {
        OauthClient client = oauthClientRepository.findById(clientId).orElseThrow(() -> {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("clientId", "클라이언트를 찾을 수 없습니다").
                    build()
            );
        });
        List<String> uris = client.getRedirectUris()
                .stream().map(OauthRedirectUri::toUriString)
                .toList();
        if (uris.contains(redirectURI))
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("redirectURI", "리다이렉트 주소가 올바르지 않습니다").
                    build()
            );
        if (client.getAccess() != OauthAccessType.ALL && client.getAccess() != OauthAccessType.valueOf(user.getRole().name())) {
            String msg;
            switch (client.getAccess()) {
                case STUDENT -> msg = "해당 서비스는 학생만 사용할 수 있습니다";
                case TEACHER -> msg = "해당 서비스는 선생님만 사용할 수 있습니다";
                default -> throw new InternalServerException();
            }
            throw new ForbiddenException(msg);
        }
        return client;
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
                .usercode(authCode.getUserCode())
                .oauthClient(client)
                .build();
        oauthTokenRepository.save(newToken);

        return OauthTokenResponseDto.builder()
                .token(newToken.getToken())
                .build();
    }

    public OauthResourceResponseDto getResource(OauthGetResourceRequest dto) {
        OauthToken token = oauthTokenRepository.findByTokenAndExpire(dto.getToken(), false).orElseThrow(
                () -> {throw new NotFoundException("토큰을 찾을 수 없습니다");}
        );
        OauthClient client = token.getOauthClient();
        if ( !(client.getId().equals(dto.getClientId()) && client.getClientSecret().equals(dto.getClientSecret())) ) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("client", "클라이언트 정보가 잘못되었습니다").
                    build()
            );
        }

        User user = userRepository.findById(token.getUsercode()).orElseThrow(
                () -> {throw new NotFoundException("유저를 찾을 수 없습니다");}
        );

        List<String> scopeList = new ArrayList<>();
        client.getScopes().forEach(scope -> {
            scopeList.add(scope.getOauthScope().getId());
        });

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
                .user(
                        oauthUserDto
                                .role(user.getRole())
                                .build()
                )
                .build();
    }

    @Transactional
    public void createClient(User user, CreateOauthClientRequest dto) {
        dto.getRedirectUriList().forEach(uri -> {
            oauthFacade.uriCheck(dto.getDomain(), uri);
        });

        OauthClient client = dto.toEntity(user);

        oauthClientRepository.save(client);
        oauthClientScopeRepository.saveAll(
                dto.toScopeEntitySet(client.getId(), oauthScopeProvider)
        );
        oauthRedirectUriRepository.saveAll(
                dto.toRedirectEntitySet(client.getId())
        );
    }

    public List<OauthClientResponseDto> getClientList(User user) {
        List<OauthClient> clientList = oauthClientRepository.findByUserCode(user.getCode());

        List<OauthClientResponseDto> clientResponseDtoList = new ArrayList<>();

        clientList.forEach(client -> {
            List<String> scopeList = new ArrayList<>();
            client.getScopes().forEach(scope -> {
                scopeList.add(scope.getOauthScope().getId());
            });

            clientResponseDtoList.add(
                    OauthClientResponseDto.builder()
                            .clientId(client.getId())
                            .clientSecret(client.getClientSecret())
                            .domain(client.getDomain())
                            .serviceName(client.getServiceName())
                            .redirectUriList(
                                    client.getRedirectUris().stream()
                                            .map(OauthRedirectUri::toUriString)
                                            .toList()
                            )
                            .scopeList(scopeList)
                            .access(client.getAccess())
                            .build()
            );
        });

        return clientResponseDtoList;
    }

    public List<OauthScope> getScopeList() {
        return oauthScopeProvider.getAllScope();
    }

    @Transactional
    public void deleteClient(User user, String clientId) {
        OauthClient client = oauthClientRepository.findById(clientId).orElseThrow(
                () -> {throw new NotFoundException("클라이언트를 찾을 수 없습니다");}
        );
        if (!client.getUserCode().equals(user.getCode())) {
            throw new ForbiddenException("권한이 없습니다");
        }

        oauthClientRepository.deleteById(clientId);
    }
}