package bssm.bsmauth.oauth.repositories;

import bssm.bsmauth.oauth.entities.OauthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthClientRepository extends JpaRepository<OauthClient, String> {}
