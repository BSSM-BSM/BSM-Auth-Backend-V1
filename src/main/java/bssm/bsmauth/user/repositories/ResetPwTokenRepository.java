package bssm.bsmauth.user.repositories;

import bssm.bsmauth.user.entities.ResetPwToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPwTokenRepository extends JpaRepository <ResetPwToken, String> {

    Optional<ResetPwToken> findByToken(String token);
}
