package bssm.bsmauth.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UpdatePwRequest {

    @NotBlank
    @Size(min = 8, max = 24)
    private String newPw;

    @NotBlank
    @Size(min = 8, max = 24)
    private String checkNewPw;
}
