package bssm.bsmauth.domain.user.entities;

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
    private String uniqNo;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private boolean codeAvailable;

    @Column(length = 8)
    private String authCode;

    @Column(columnDefinition = "INT(1) UNSIGNED")
    private int level;

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

    public void setCodeAvailable(boolean codeAvailable) {
        this.codeAvailable = codeAvailable;
    }

    @Builder
    public Student(String uniqNo, boolean codeAvailable, String authCode, int level, int enrolledAt, int grade, int classNo, int studentNo, String name, String email) {
        this.uniqNo = uniqNo;
        this.codeAvailable = codeAvailable;
        this.authCode = authCode;
        this.level = level;
        this.enrolledAt = enrolledAt;
        this.grade = grade;
        this.classNo = classNo;
        this.studentNo = studentNo;
        this.name = name;
        this.email = email;
    }
}
