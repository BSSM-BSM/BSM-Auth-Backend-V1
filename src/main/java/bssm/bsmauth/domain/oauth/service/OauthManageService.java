package bssm.bsmauth.domain.oauth.service;

import bssm.bsmauth.domain.oauth.domain.*;
import bssm.bsmauth.domain.oauth.domain.repository.OauthClientScopeRepository;
import bssm.bsmauth.domain.oauth.domain.repository.OauthRedirectUriRepository;
import bssm.bsmauth.domain.oauth.facade.OauthManageFacade;
import bssm.bsmauth.domain.oauth.presentation.dto.req.CreateOauthClientReq;
import bssm.bsmauth.domain.oauth.presentation.dto.req.UpdateOauthClientReq;
import bssm.bsmauth.domain.oauth.presentation.dto.res.*;
import bssm.bsmauth.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class OauthManageService {

    private final OauthClientScopeRepository oauthClientScopeRepository;
    private final OauthRedirectUriRepository oauthRedirectUriRepository;
    private final OauthScopeProvider oauthScopeProvider;
    private final OauthManageFacade oauthManageFacade;

    @Transactional
    public void createClient(User user, CreateOauthClientReq req) {
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

    public List<OauthClientRes> getClientList(User user) {
        List<OauthClient> clientList = oauthManageFacade.findAllByUser(user);

        return clientList.stream()
                .map(OauthClient::toResponse)
                .toList();
    }

    public List<OauthScope> getScopeList() {
        return oauthScopeProvider.getAllScope();
    }

    @Transactional
    public void updateClient(User user, String clientId, UpdateOauthClientReq req) {
        OauthClient client = oauthManageFacade.findById(clientId);
        oauthManageFacade.permissionCheck(client, user);
        req.getRedirectUriList()
                .forEach(uri -> oauthManageFacade.uriCheck(req.getDomain(), uri));

        req.updateClient(client);
        updateRedirect(client, req);
        updateScope(client, req);
    }

    @Transactional
    public void deleteClient(User user, String clientId) {
        OauthClient client = oauthManageFacade.findById(clientId);
        oauthManageFacade.permissionCheck(client, user);

        oauthManageFacade.delete(client);
    }

    private void updateRedirect(OauthClient client, UpdateOauthClientReq req) {
        Set<OauthRedirectUri> newRedirectSet = req.toRedirectEntitySet(client.getId());
        Set<OauthRedirectUri> deleteRedirectSet = client.getRedirectUris();
        newRedirectSet.forEach(deleteRedirectSet::remove);

        oauthRedirectUriRepository.deleteAll(deleteRedirectSet);
        oauthRedirectUriRepository.saveAll(newRedirectSet);
    }

    private void updateScope(OauthClient client, UpdateOauthClientReq req) {
        Set<OauthClientScope> newScopeSet = req.toScopeEntitySet(client.getId(), oauthScopeProvider);
        Set<OauthClientScope> deleteScopeSet = client.getScopes();
        newScopeSet.forEach(deleteScopeSet::remove);

        oauthClientScopeRepository.deleteAll(deleteScopeSet);
        oauthClientScopeRepository.saveAll(newScopeSet);
    }
}
