package bssm.bsmauth.domain.oauth.domain.repository;

import bssm.bsmauth.domain.oauth.domain.OauthAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthAuthCodeRepository extends JpaRepository<OauthAuthCode, String> {

    Optional<OauthAuthCode> findByCode(String code);
}
