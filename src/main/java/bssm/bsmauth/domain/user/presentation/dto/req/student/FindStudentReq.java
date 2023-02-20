package bssm.bsmauth.domain.user.presentation.dto.req.student;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindStudentReq {

    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
}
