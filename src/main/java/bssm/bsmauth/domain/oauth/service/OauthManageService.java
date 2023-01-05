package bssm.bsmauth.domain.oauth.service;

import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.repository.OauthClientScopeRepository;
import bssm.bsmauth.domain.oauth.domain.repository.OauthRedirectUriRepository;
import bssm.bsmauth.domain.oauth.facade.OauthFacade;
import bssm.bsmauth.domain.oauth.presentation.dto.request.CreateOauthClientRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.UpdateOauthClientRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.response.*;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.error.exceptions.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OauthManageService {

    private final OauthClientScopeRepository oauthClientScopeRepository;
    private final OauthRedirectUriRepository oauthRedirectUriRepository;
    private final OauthScopeProvider oauthScopeProvider;
    private final OauthFacade oauthFacade;

    @Transactional
    public void createClient(User user, CreateOauthClientRequest req) {
        req.getRedirectUriList()
                .forEach(uri -> oauthFacade.uriCheck(req.getDomain(), uri));

        OauthClient client = req.toEntity(user);

        oauthFacade.save(client);
        oauthClientScopeRepository.saveAll(
                req.toScopeEntitySet(client.getId(), oauthScopeProvider)
        );
        oauthRedirectUriRepository.saveAll(
                req.toRedirectEntitySet(client.getId())
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
    public void updateClient(User user, String clientId, UpdateOauthClientRequest req) {
        OauthClient client = oauthFacade.findById(clientId);
        if (!client.getUserCode().equals(user.getCode())) {
            throw new ForbiddenException("권한이 없습니다");
        }

        req.updateClient(client);

        Set<OauthClientScope> newScopeSet = req.toScopeEntitySet(client.getId(), oauthScopeProvider);
        Set<OauthClientScope> deleteSet = client.getScopes();
        newScopeSet.forEach(deleteSet::remove);

        oauthClientScopeRepository.deleteAll(deleteSet);
        oauthClientScopeRepository.saveAll(newScopeSet);
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
