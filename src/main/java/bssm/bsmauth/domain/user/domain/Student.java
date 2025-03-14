package bssm.bsmauth.domain.user.domain;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @Column(length = 10)
    private String id;

    @Column(name = "code_available", nullable = false)
    private boolean codeAvailable;

    @Column(length = 8, nullable = false)
    private String authCode;

    @Column(name = "enrolled_at", columnDefinition = "year", nullable = false)
    private int enrolledAt;

    @Column(columnDefinition = "INT(1) UNSIGNED", nullable = false)
    private int grade;

    @Column(columnDefinition = "INT(1) UNSIGNED", nullable = false)
    private int classNo;

    @Column(columnDefinition = "INT(2) UNSIGNED", nullable = false)
    private int studentNo;

    @Column(length = 8, nullable = false)
    private String name;

    @Column(length = 32, nullable = false)
    private String email;

    public void expireAuthCode() {
        this.codeAvailable = false;
    }

    public Integer getCardinal() {
        return this.enrolledAt - 2020;
    }

    public boolean isGraduate() {
        return this.grade == 0 && this.classNo == 0 && this.studentNo == 0;
    }

    @Builder
    public Student(String id, boolean codeAvailable, String authCode, int enrolledAt, int grade, int classNo, int studentNo, String name, String email) {
        this.id = id;
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
