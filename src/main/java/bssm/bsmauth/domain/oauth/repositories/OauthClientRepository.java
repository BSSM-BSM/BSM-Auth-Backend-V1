package bssm.bsmauth.domain.oauth.repositories;

import bssm.bsmauth.domain.oauth.entities.OauthClient;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OauthClientRepository extends JpaRepository<OauthClient, String> {

    @EntityGraph(attributePaths = "scopes")
    List<OauthClient> findByUsercode(int usercode);
}
