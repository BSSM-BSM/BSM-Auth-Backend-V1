package bssm.bsmauth.domain.user.presentation.dto.response.student;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentInfoResponse {

    private String name;
    private int enrolledAt;
    private int grade;
    private int classNo;
    private int studentNo;

}
