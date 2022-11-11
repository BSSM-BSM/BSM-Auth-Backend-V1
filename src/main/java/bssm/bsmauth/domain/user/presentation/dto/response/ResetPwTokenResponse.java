package bssm.bsmauth.domain.user.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ResetPwTokenResponse {

    private boolean used;
    private Date expireIn;
}
