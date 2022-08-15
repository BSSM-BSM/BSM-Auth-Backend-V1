package bssm.bsmauth.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ResetPwTokenInfoDto {

    private boolean used;
    private Date expireIn;
}
