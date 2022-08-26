package bssm.bsmauth.domain.oauth.repositories;

import bssm.bsmauth.domain.oauth.entities.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, String> {

    Optional<OauthToken> findByUsercodeAndClientId(int usercode, String clientId);

    Optional<OauthToken> findByTokenAndExpire(String token, boolean expire);
}
