package bssm.bsmauth.domain.user.domain.repository;

import bssm.bsmauth.domain.user.domain.Student;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.type.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long>, UserRepositoryCustom {

    Optional<User> findById(String userId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByStudent(Student student);

    Optional<User> findByRoleAndTeacherEmail(UserRole role, String email);
}
