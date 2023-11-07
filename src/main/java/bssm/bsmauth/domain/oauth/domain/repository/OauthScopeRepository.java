package bssm.bsmauth.domain.oauth.domain.repository;

import bssm.bsmauth.domain.oauth.domain.OauthScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OauthScopeRepository extends JpaRepository<OauthScope, String> {

    List<OauthScope> findAllByOrderByIdxAsc();
}
