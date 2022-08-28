package bssm.bsmauth.domain.oauth.repositories;

import bssm.bsmauth.domain.oauth.entities.OauthClientScope;
import bssm.bsmauth.domain.oauth.entities.OauthClientScopePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OauthClientScopeRepository extends JpaRepository<OauthClientScope, OauthClientScopePk> {

    List<OauthClientScope> findAllByOauthClientScopePkClientId(String clientId);
}