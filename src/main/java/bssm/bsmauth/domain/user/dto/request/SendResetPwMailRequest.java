
package bssm.bsmauth.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SendResetPwMailRequest {

    @NotBlank
    @Size(min = 2, max = 20)
    private String id;
}
