package bssm.bsmauth.domain.user.repositories;

import bssm.bsmauth.domain.user.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository <Student, String> {

    Optional<Student> findByAuthCode(String authCode);

    Optional<Student> findByEnrolledAtAndGradeAndClassNoAndStudentNoAndName(int enrolledAt, int grade, int classNo, int studentNo, String name);
}
