package bssm.bsmauth.domain.user.presentation.dto.res.student;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentRes {

    private String name;
    private int enrolledAt;
    private int grade;
    private int classNo;
    private int studentNo;

}
