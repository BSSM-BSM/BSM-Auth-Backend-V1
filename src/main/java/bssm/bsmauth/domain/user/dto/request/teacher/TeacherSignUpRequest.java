package bssm.bsmauth.domain.user.dto.request.teacher;

import bssm.bsmauth.domain.user.dto.request.UserSignUpRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class TeacherSignUpRequest extends UserSignUpRequest {

    @NotBlank
    @Size(min = 1, max = 8)
    private String name;
}
