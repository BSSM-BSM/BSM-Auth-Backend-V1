package bssm.bsmauth.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResetPwByTokenRequest extends UpdatePwRequest {

    private String token;
}
