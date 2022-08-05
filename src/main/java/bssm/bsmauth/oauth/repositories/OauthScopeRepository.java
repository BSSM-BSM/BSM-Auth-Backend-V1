package bssm.bsmauth.oauth.repositories;

import bssm.bsmauth.oauth.entities.OauthScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthScopeRepository extends JpaRepository<OauthScope, String> {}
