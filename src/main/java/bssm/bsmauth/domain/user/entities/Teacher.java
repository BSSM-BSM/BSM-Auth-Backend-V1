package bssm.bsmauth.domain.user.entities;

import bssm.bsmauth.domain.user.dto.response.teacher.TeacherResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Builder
    public Teacher(Long teacherId, String name, String email) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
    }

    public TeacherResponseDto teacherInfo() {
        return TeacherResponseDto.builder()
                .name(name)
                .build();
    }
}
