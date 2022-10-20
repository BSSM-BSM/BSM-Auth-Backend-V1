package bssm.bsmauth.domain.user.presentation.dto.request.student;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindStudentRequest {

    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
}
