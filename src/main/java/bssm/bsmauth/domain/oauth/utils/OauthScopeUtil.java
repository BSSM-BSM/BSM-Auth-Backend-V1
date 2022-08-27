package bssm.bsmauth.domain.oauth.utils;

import bssm.bsmauth.global.exception.exceptions.NotFoundException;
import bssm.bsmauth.domain.oauth.entities.OauthScope;
import bssm.bsmauth.domain.oauth.repositories.OauthScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthScopeUtil {

    private final OauthScopeRepository oauthScopeRepository;
    private final HashMap<String, OauthScope> scopeList = new HashMap<>();

    @PostConstruct
    private void init() {
        List<OauthScope> scopes = oauthScopeRepository.findAll();
        scopes.forEach(scope -> {
            scopeList.put(scope.getId(), scope);
        });
    }

    public OauthScope getScope(String id) throws NotFoundException {
        OauthScope scope = scopeList.get(id);
        if (scope == null) throw new NotFoundException("스코프를 찾을 수 없습니다");
        return scope;
    }

    public List<OauthScope> getAllScope() {
        return scopeList.values().stream().toList();
    }
}
