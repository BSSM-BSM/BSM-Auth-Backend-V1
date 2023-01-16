package bssm.bsmauth.domain.oauth.domain.repository;

import bssm.bsmauth.domain.oauth.domain.OauthRedirectUri;
import bssm.bsmauth.domain.oauth.domain.OauthRedirectUriPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthRedirectUriRepository extends JpaRepository<OauthRedirectUri, OauthRedirectUriPk> {}
