package bssm.bsmauth.oauth.repositories;

import bssm.bsmauth.oauth.entities.OauthAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthAuthCodeRepository extends JpaRepository<OauthAuthCode, String> {}
