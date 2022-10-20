package bssm.bsmauth.domain.user.presentation.dto.request.teacher;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
public class TeacherEmailDto {

    @Email(
            message = "올바른 선생님 이메일 주소가 아닙니다"
    )
    private String email;
}
