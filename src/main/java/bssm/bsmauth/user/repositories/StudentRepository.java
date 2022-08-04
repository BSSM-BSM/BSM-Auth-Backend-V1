package bssm.bsmauth.user.repositories;

import bssm.bsmauth.user.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository <Student, String> {

    Optional<Student> findByAuthCode(String authCode);
}
