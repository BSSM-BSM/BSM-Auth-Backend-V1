package bssm.bsmauth.domain.oauth.service;

import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.repository.OauthClientScopeRepository;
import bssm.bsmauth.domain.oauth.domain.repository.OauthRedirectUriRepository;
import bssm.bsmauth.domain.oauth.facade.OauthManageFacade;
import bssm.bsmauth.domain.oauth.presentation.dto.request.AddOauthClientRedirectRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.CreateOauthClientRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.RemoveOauthClientRedirectRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.request.UpdateOauthClientRequest;
import bssm.bsmauth.domain.oauth.presentation.dto.response.*;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
public class OauthManageService {

    private final OauthClientScopeRepository oauthClientScopeRepository;
    private final OauthRedirectUriRepository oauthRedirectUriRepository;
    private final OauthScopeProvider oauthScopeProvider;
    private final OauthManageFacade oauthManageFacade;

    @Transactional
    public void createClient(User user, CreateOauthClientRequest req) {
        req.getRedirectUriList()
                .forEach(uri -> oauthManageFacade.uriCheck(req.getDomain(), uri));

        OauthClient client = req.toEntity(user);

        oauthManageFacade.save(client);
        oauthClientScopeRepository.saveAll(
                req.toScopeEntitySet(client.getId(), oauthScopeProvider)
        );
        oauthRedirectUriRepository.saveAll(
                req.toRedirectEntitySet(client.getId())
        );
    }

    public List<OauthClientResponseDto> getClientList(User user) {
        List<OauthClient> clientList = oauthManageFacade.findAllByUser(user);

        return clientList.stream()
                .map(OauthClient::toResponse)
                .toList();
    }

    public List<OauthScope> getScopeList() {
        return oauthScopeProvider.getAllScope();
    }

    @Transactional
    public void updateClient(User user, String clientId, UpdateOauthClientRequest req) {
        OauthClient client = oauthManageFacade.findById(clientId);
        oauthManageFacade.permissionCheck(client, user);

        req.updateClient(client);

        Set<OauthClientScope> newScopeSet = req.toScopeEntitySet(client.getId(), oauthScopeProvider);
        Set<OauthClientScope> deleteSet = client.getScopes();
        newScopeSet.forEach(deleteSet::remove);

        oauthClientScopeRepository.deleteAll(deleteSet);
        oauthClientScopeRepository.saveAll(newScopeSet);
    }

    @Transactional
    public void deleteClient(User user, String clientId) {
        OauthClient client = oauthManageFacade.findById(clientId);
        oauthManageFacade.permissionCheck(client, user);

        oauthManageFacade.delete(client);
    }

    public void addRedirectUri(User user, @Valid AddOauthClientRedirectRequest req) {
        OauthClient client = oauthManageFacade.findById(req.getClientId());
        oauthManageFacade.permissionCheck(client, user);
        oauthManageFacade.uriCheck(client.getDomain(), req.getRedirectUri());

        oauthRedirectUriRepository.save(req.toEntity());
    }

    @Transactional
    public void removeRedirectUri(User user, @Valid RemoveOauthClientRedirectRequest req) {
        OauthClient client = oauthManageFacade.findById(req.getClientId());
        oauthManageFacade.permissionCheck(client, user);
        OauthRedirectUri redirectUri = oauthRedirectUriRepository.findByOauthClientAndPkRedirectUri(client, req.getRedirectUri())
                .orElseThrow(() -> new NotFoundException("리다이렉트 URI를 찾을 수 없습니다"));

        oauthRedirectUriRepository.delete(redirectUri);
    }
}
