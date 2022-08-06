package bssm.bsmauth.oauth;

import bssm.bsmauth.global.exceptions.BadRequestException;
import bssm.bsmauth.global.exceptions.NotFoundException;
import bssm.bsmauth.oauth.dto.OauthUserDto;
import bssm.bsmauth.oauth.dto.request.CreateOauthClientDto;
import bssm.bsmauth.oauth.dto.request.OauthAuthorizationDto;
import bssm.bsmauth.oauth.dto.request.OauthGetResourceDto;
import bssm.bsmauth.oauth.dto.request.OauthGetTokenDto;
import bssm.bsmauth.oauth.dto.response.*;
import bssm.bsmauth.oauth.entities.*;
import bssm.bsmauth.oauth.repositories.OauthAuthCodeRepository;
import bssm.bsmauth.oauth.repositories.OauthClientRepository;
import bssm.bsmauth.oauth.repositories.OauthClientScopeRepository;
import bssm.bsmauth.oauth.repositories.OauthTokenRepository;
import bssm.bsmauth.oauth.utils.OauthScopeUtil;
import bssm.bsmauth.user.entities.User;
import bssm.bsmauth.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthClientRepository oauthClientRepository;
    private final OauthClientScopeRepository oauthClientScopeRepository;
    private final OauthAuthCodeRepository oauthAuthCodeRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final UserRepository userRepository;
    private final OauthScopeUtil oauthScopeUtil;

    public OauthAuthenticationResponseDto authentication(User user, String clientId, String redirectURI) {
        OauthClient client = oauthClientRepository.findById(clientId).orElseThrow(
                () -> {throw new BadRequestException("Oauth Authentication Failed");}
        );
        if (!client.getRedirectURI().equals(redirectURI)) throw new BadRequestException("Oauth Authentication Failed");

        // 이미 인증이 되었다면
        if (oauthTokenRepository.findByUsercode(user.getUsercode()).isPresent()) {
            return OauthAuthenticationResponseDto.builder()
                    .isAuthorized(true)
                    .build();
        }

        List<String> scopeList = new ArrayList<>();
        client.getScopes().forEach(scope -> {
            scopeList.add(scope.getOauthScope().getId());
        });

        return OauthAuthenticationResponseDto.builder()
                .isAuthorized(false)
                .domain(client.getDomain())
                .serviceName(client.getServiceName())
                .scopeList(scopeList)
                .build();
    }

    public OauthAuthorizationResponseDto authorization(User user, OauthAuthorizationDto dto) {
        OauthClient client = oauthClientRepository.findById(dto.getClientId()).orElseThrow(
                () -> {throw new BadRequestException("Oauth Authentication Failed");}
        );
        if (!client.getRedirectURI().equals(dto.getRedirectURI())) throw new BadRequestException("Oauth Authentication Failed");

        OauthAuthCode authCode = OauthAuthCode.builder()
                .code(getRandomStr(32))
                .usercode(user.getUsercode())
                .oauthClient(client)
                .build();
        oauthAuthCodeRepository.save(authCode);

        return OauthAuthorizationResponseDto.builder()
                .redirectURI(client.getRedirectURI() + "?code=" + authCode.getCode())
                .build();
    }

    public OauthTokenResponseDto getToken(OauthGetTokenDto dto) {
        OauthAuthCode authCode = oauthAuthCodeRepository.findByCodeAndExpire(dto.getAuthCode(), false).orElseThrow(
                () -> {throw new NotFoundException("인증 코드를 찾을 수 없습니다");}
        );
        OauthClient client = authCode.getOauthClient();
        if ( !(client.getId().equals(dto.getClientId()) && client.getClientSecret().equals(dto.getClientSecret())) ) {
            throw new BadRequestException("클라이언트 정보가 잘못되었습니다");
        }

        authCode.setExpire(true);
        oauthAuthCodeRepository.save(authCode);

        Optional<OauthToken> token = oauthTokenRepository.findByUsercode(authCode.getUsercode());
        if (token.isPresent()) {
            return OauthTokenResponseDto.builder()
                    .token(token.get().getToken())
                    .build();
        }

        OauthToken newToken = OauthToken.builder()
                .token(getRandomStr(32))
                .usercode(authCode.getUsercode())
                .oauthClient(client)
                .build();
        oauthTokenRepository.save(newToken);

        return OauthTokenResponseDto.builder()
                .token(newToken.getToken())
                .build();
    }

    public OauthResourceResponseDto getResource(OauthGetResourceDto dto) {
        OauthToken token = oauthTokenRepository.findByTokenAndExpire(dto.getToken(), false).orElseThrow(
                () -> {throw new NotFoundException("토큰을 찾을 수 없습니다");}
        );
        OauthClient client = token.getOauthClient();
        if ( !(client.getId().equals(dto.getClientId()) && client.getClientSecret().equals(dto.getClientSecret())) ) {
            throw new BadRequestException("클라이언트 정보가 잘못되었습니다");
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
            switch (scope) {
                case "code" -> oauthUserDto.code(user.getUsercode());
                case "nickname" -> oauthUserDto.nickname(user.getNickname());
                case "enrolledAt" -> oauthUserDto.enrolledAt(user.getStudent().getEnrolledAt());
                case "grade" -> oauthUserDto.grade(user.getStudent().getGrade());
                case "classNo" -> oauthUserDto.classNo(user.getStudent().getClassNo());
                case "studentNo" -> oauthUserDto.studentNo(user.getStudent().getStudentNo());
                case "name" -> oauthUserDto.name(user.getStudent().getName());
                case "email" -> oauthUserDto.email(user.getStudent().getEmail());
            }
        });

        return OauthResourceResponseDto.builder()
                .scopeList(scopeList)
                .user(oauthUserDto.build())
                .build();
    }

    public void createClient(User user, CreateOauthClientDto dto) {
        if (!domainCheck(dto.getDomain())) throw new BadRequestException("도메인이 잘못되었습니다");
        if (!uriCheck(dto.getDomain(), dto.getRedirectURI())) throw new BadRequestException("리다이렉트 주소가 잘못되었습니다");

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

    public List<OauthClientResponseDto> getClientList(User user) {
        List<OauthClient> clientList = oauthClientRepository.findByUsercode(user.getUsercode());

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
                            .redirectURI(client.getRedirectURI())
                            .usercode(client.getUsercode())
                            .scopeList(scopeList)
                            .build()
            );
        });

        return clientResponseDtoList;
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
