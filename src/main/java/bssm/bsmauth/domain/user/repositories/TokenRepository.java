package bssm.bsmauth.domain.user.repositories;

import bssm.bsmauth.domain.user.entities.UserToken;
import bssm.bsmauth.domain.user.type.UserTokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TokenRepository extends JpaRepository <UserToken, String> {

    Optional<UserToken> findByTokenAndType(String token, UserTokenType type);
}
