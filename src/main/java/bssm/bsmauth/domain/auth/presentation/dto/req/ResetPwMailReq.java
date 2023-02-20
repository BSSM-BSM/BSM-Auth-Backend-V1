
package bssm.bsmauth.domain.auth.presentation.dto.req;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ResetPwMailReq {

    @NotBlank
    @Size(
            min = 2,
            max = 20,
            message = "id는 2 ~ 20글자여야 합니다"
    )
    private String id;
}
