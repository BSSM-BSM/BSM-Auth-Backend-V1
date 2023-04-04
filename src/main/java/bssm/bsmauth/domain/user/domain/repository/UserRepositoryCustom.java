package bssm.bsmauth.domain.user.domain.repository;

import bssm.bsmauth.domain.user.domain.User;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findNicknameHistory(String nickname);

}
