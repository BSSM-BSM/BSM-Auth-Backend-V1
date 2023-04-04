package bssm.bsmauth.domain.user.presentation.dto.res.student;

import bssm.bsmauth.domain.user.domain.Student;
import bssm.bsmauth.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentRes {

    private String name;
    private int enrolledAt;
    private int grade;
    private int classNo;
    private int studentNo;

    public static StudentRes ofStudent(Student student) {
        StudentRes res = new StudentRes();
        res.name = student.getName();
        res.enrolledAt = student.getEnrolledAt();
        res.grade = student.getGrade();
        res.classNo = student.getClassNo();
        res.studentNo = student.getStudentNo();
        return res;
    }

    public static StudentRes ofUser(User user) {
        if (user.getStudent() == null) {
            return null;
        }
        return ofStudent(user.getStudent());
    }
}
