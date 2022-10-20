package bssm.bsmauth.domain.oauth.domain.repositories;

import bssm.bsmauth.domain.oauth.domain.OauthClientScope;
import bssm.bsmauth.domain.oauth.domain.OauthClientScopePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OauthClientScopeRepository extends JpaRepository<OauthClientScope, OauthClientScopePk> {

    List<OauthClientScope> findAllByOauthClientScopePkClientId(String clientId);
}
