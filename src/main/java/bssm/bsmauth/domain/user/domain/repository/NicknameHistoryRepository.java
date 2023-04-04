package bssm.bsmauth.domain.user.domain.repository;

import bssm.bsmauth.domain.user.domain.NicknameHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NicknameHistoryRepository extends JpaRepository <NicknameHistory, Long> {}
