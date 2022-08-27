package bssm.bsmauth.domain.user.repositories;

import bssm.bsmauth.domain.user.entities.Student;
import bssm.bsmauth.domain.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {

    Optional<User> findById(String userId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByStudent(Student student);
}
