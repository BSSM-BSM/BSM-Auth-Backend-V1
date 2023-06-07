package bssm.bsmauth.domain.user.domain.repository;

import bssm.bsmauth.domain.user.domain.ForbiddenNickname;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForbiddenNicknameRepository extends JpaRepository <ForbiddenNickname, String> {

    boolean existsByNickname(String nickname);
}
