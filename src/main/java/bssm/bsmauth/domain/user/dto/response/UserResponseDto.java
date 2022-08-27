package bssm.bsmauth.domain.user.dto.response;

import bssm.bsmauth.domain.user.type.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private Long code;
    private String nickname;
    private UserRole role;
    private StudentResponseDto student;
}
