package bssm.bsmauth.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentResponseDto {

    private int enrolledAt;
    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
}
