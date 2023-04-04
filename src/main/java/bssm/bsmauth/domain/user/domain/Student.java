package bssm.bsmauth.domain.user.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @Column(length = 10)
    private String studentId;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private boolean codeAvailable;

    @Column(length = 8)
    private String authCode;

    @Column(columnDefinition = "year")
    private int enrolledAt;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private int grade;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private int classNo;

    @Column(columnDefinition = "INT(2) UNSIGNED")
    private int studentNo;

    @Column(length = 8)
    private String name;

    @Column(length = 32)
    private String email;

    public void expireAuthCode() {
        this.codeAvailable = false;
    }

    @Builder
    public Student(String studentId, boolean codeAvailable, String authCode, int enrolledAt, int grade, int classNo, int studentNo, String name, String email) {
        this.studentId = studentId;
        this.codeAvailable = codeAvailable;
        this.authCode = authCode;
        this.enrolledAt = enrolledAt;
        this.grade = grade;
        this.classNo = classNo;
        this.studentNo = studentNo;
        this.name = name;
        this.email = email;
    }

}
