package bssm.bsmauth.domain.oauth.service;

import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.repository.OauthClientScopeRepository;
import bssm.bsmauth.domain.oauth.domain.repository.OauthRedirectUriRepository;
import bssm.bsmauth.domain.oauth.facade.OauthFacade;
import bssm.bsmauth.domain.oauth.presentation.dto.request.CreateOauthClientRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.response.*;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.error.exceptions.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthManageService {

    private final OauthClientScopeRepository oauthClientScopeRepository;
    private final OauthRedirectUriRepository oauthRedirectUriRepository;
    private final OauthScopeProvider oauthScopeProvider;
    private final OauthFacade oauthFacade;

    @Transactional
    public void createClient(User user, CreateOauthClientRequest dto) {
        dto.getRedirectUriList()
                .forEach(uri -> oauthFacade.uriCheck(dto.getDomain(), uri));

        OauthClient client = dto.toEntity(user);

        oauthFacade.save(client);
        oauthClientScopeRepository.saveAll(
                dto.toScopeEntitySet(client.getId(), oauthScopeProvider)
        );
        oauthRedirectUriRepository.saveAll(
                dto.toRedirectEntitySet(client.getId())
        );
    }

    public List<OauthClientResponseDto> getClientList(User user) {
        List<OauthClient> clientList = oauthFacade.findAllByUser(user);

        return clientList.stream()
                .map(OauthClient::toResponse)
                .toList();
    }

    public List<OauthScope> getScopeList() {
        return oauthScopeProvider.getAllScope();
    }

    @Transactional
    public void deleteClient(User user, String clientId) {
        OauthClient client = oauthFacade.findById(clientId);
        if (!client.getUserCode().equals(user.getCode())) {
            throw new ForbiddenException("권한이 없습니다");
        }

        oauthFacade.delete(client);
    }
}
