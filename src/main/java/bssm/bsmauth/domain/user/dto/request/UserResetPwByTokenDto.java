package bssm.bsmauth.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResetPwByTokenDto extends UserUpdatePwDto {

    private String token;
}
