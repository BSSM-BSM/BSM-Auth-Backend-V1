package bssm.bsmauth.domain.oauth.domain.repository;

import bssm.bsmauth.domain.oauth.domain.OauthClient;
import bssm.bsmauth.domain.oauth.domain.OauthToken;
import bssm.bsmauth.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, String> {

    Optional<OauthToken> findByUserAndOauthClient(User user, OauthClient oauthClient);

    Optional<OauthToken> findByTokenAndIsExpired(String token, boolean isExpired);
}
