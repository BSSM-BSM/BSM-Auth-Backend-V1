package bssm.bsmauth.domain.user.repositories;

import bssm.bsmauth.domain.user.entities.Student;
import bssm.bsmauth.domain.user.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TeacherRepository extends JpaRepository <Teacher, String> {

    Optional<Student> findByEmail(String email);
}
