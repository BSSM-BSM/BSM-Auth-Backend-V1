package bssm.bsmauth.domain.user.dto.request.teacher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class SendTeacherAuthCodeMailDto {

    private String email;
}
