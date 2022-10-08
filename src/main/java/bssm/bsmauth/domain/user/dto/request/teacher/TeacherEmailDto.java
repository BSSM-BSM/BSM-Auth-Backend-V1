package bssm.bsmauth.domain.user.dto.request.teacher;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
public class TeacherEmailDto {

    @Email
    private String email;
}
