package bssm.bsmauth.domain.user.presentation.dto.req.student;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindStudentReq {

    @Min(1)
    @Max(3)
    private int grade;

    @Min(1)
    @Max(4)
    private int classNo;

    @Min(1)
    @Max(17)
    private int studentNo;

    @NotBlank
    private String name;
}
