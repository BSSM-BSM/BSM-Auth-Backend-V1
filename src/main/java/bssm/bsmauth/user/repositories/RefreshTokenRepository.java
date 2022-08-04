package bssm.bsmauth.user.repositories;

import bssm.bsmauth.user.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository <RefreshToken, String> {

    Optional<RefreshToken> findByTokenAndIsAvailable(String token, boolean isAvailable);
}
