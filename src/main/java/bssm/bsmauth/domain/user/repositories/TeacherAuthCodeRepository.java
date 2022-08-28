package bssm.bsmauth.domain.user.repositories;

import bssm.bsmauth.domain.user.entities.TeacherAuthCode;
import bssm.bsmauth.domain.user.type.UserTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherAuthCodeRepository extends JpaRepository <TeacherAuthCode, String> {

    Optional<TeacherAuthCode> findByTokenAndType(String token, UserTokenType type);

    @Modifying
    @Query("DELETE FROM TeacherAuthCode t WHERE t.email=:email")
    void deleteByEmail(@Param("email") String email);
}
