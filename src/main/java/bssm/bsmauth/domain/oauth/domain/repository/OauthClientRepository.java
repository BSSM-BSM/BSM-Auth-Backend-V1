package bssm.bsmauth.domain.oauth.domain.repository;

import bssm.bsmauth.domain.oauth.domain.OauthClient;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OauthClientRepository extends JpaRepository<OauthClient, String> {

    @EntityGraph(attributePaths = "scopes")
    List<OauthClient> findByUsercode(Long usercode);
}
