package bssm.bsmauth.domain.oauth.domain.repository;

import bssm.bsmauth.domain.oauth.domain.OauthToken;
import bssm.bsmauth.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, String> {

    Optional<OauthToken> findByUserAndClientId(User user, String clientId);

    Optional<OauthToken> findByTokenAndExpire(String token, boolean expire);
}
