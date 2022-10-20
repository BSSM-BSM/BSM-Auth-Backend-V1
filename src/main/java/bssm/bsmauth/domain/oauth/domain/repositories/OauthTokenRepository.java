package bssm.bsmauth.domain.oauth.domain.repositories;

import bssm.bsmauth.domain.oauth.domain.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, String> {

    Optional<OauthToken> findByUsercodeAndClientId(Long usercode, String clientId);

    Optional<OauthToken> findByTokenAndExpire(String token, boolean expire);
}
