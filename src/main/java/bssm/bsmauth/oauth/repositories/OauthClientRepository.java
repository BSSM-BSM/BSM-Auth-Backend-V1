package bssm.bsmauth.oauth.repositories;

import bssm.bsmauth.oauth.entities.OauthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OauthClientRepository extends JpaRepository<OauthClient, String> {

    List<OauthClient> findByUsercode(int usercode);
}
