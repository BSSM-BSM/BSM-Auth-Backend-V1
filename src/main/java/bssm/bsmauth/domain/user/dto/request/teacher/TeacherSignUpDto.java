package bssm.bsmauth.domain.user.dto.request.teacher;

import bssm.bsmauth.domain.user.dto.request.UserSignUpDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TeacherSignUpDto extends UserSignUpDto {

    private String authCode;
    private String name;
}
