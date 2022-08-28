package bssm.bsmauth.domain.oauth.repositories;

import bssm.bsmauth.domain.oauth.entities.OauthAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthAuthCodeRepository extends JpaRepository<OauthAuthCode, String> {

    Optional<OauthAuthCode> findByCodeAndExpire(String id, boolean expire);
}