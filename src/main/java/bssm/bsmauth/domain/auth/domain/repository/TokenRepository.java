package bssm.bsmauth.domain.auth.domain.repository;

import bssm.bsmauth.domain.auth.domain.UserToken;
import bssm.bsmauth.domain.auth.domain.UserTokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository <UserToken, String> {

    Optional<UserToken> findByTokenAndType(String token, UserTokenType type);
}
