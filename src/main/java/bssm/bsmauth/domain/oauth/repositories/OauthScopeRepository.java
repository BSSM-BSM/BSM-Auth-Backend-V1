package bssm.bsmauth.domain.oauth.repositories;

import bssm.bsmauth.domain.oauth.entities.OauthScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthScopeRepository extends JpaRepository<OauthScope, String> {}
