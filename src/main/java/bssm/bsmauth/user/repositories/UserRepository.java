package bssm.bsmauth.user.repositories;

import bssm.bsmauth.user.entities.Student;
import bssm.bsmauth.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer> {

    Optional<User> findById(String userId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByStudent(Student student);
}
