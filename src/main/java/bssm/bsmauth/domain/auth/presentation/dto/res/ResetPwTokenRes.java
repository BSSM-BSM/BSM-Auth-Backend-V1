package bssm.bsmauth.domain.auth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ResetPwTokenRes {

    private boolean used;
    private Date expireIn;
}
