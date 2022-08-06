package bssm.bsmauth.oauth.repositories;

import bssm.bsmauth.oauth.entities.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, String> {

    Optional<OauthToken> findByUsercode(int usercode);
}
