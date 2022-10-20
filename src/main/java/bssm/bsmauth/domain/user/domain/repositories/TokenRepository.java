package bssm.bsmauth.domain.user.domain.repositories;

import bssm.bsmauth.domain.user.domain.UserToken;
import bssm.bsmauth.domain.user.domain.UserTokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository <UserToken, String> {

    Optional<UserToken> findByTokenAndType(String token, UserTokenType type);
}
