package bssm.bsmauth.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserSignUpDto {

    private String id;
    private String pw;
    private String checkPw;
    private String nickname;
    private String authCode;
}
