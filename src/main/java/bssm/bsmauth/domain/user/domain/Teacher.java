package bssm.bsmauth.domain.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    @Column(length = 8)
    private String name;

    @Column(length = 32)
    private String email;

    public static Teacher create(String name, String email) {
        Teacher teacher = new Teacher();
        teacher.name = name;
        teacher.email = email;
        return teacher;
    }

}
