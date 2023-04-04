package bssm.bsmauth.domain.user.domain.repository;

import bssm.bsmauth.domain.user.domain.NicknameHistory;
import bssm.bsmauth.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NicknameHistoryRepository extends JpaRepository <NicknameHistory, Long> {

    List<User> findAllByUser(User user);
}
