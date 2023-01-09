package bssm.bsmauth.domain.oauth.domain.repository;

import bssm.bsmauth.domain.oauth.domain.OauthClient;
import bssm.bsmauth.domain.oauth.domain.OauthRedirectUri;
import bssm.bsmauth.domain.oauth.domain.OauthRedirectUriPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthRedirectUriRepository extends JpaRepository<OauthRedirectUri, OauthRedirectUriPk> {

    Optional<OauthRedirectUri> findByOauthClientAndPkRedirectUri(OauthClient client, String redirectUri);
}
