package bssm.bsmauth.domain.user.domain.repositories;

import bssm.bsmauth.domain.user.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TeacherRepository extends JpaRepository <Teacher, String> {

    Optional<Teacher> findByEmail(String email);
}
