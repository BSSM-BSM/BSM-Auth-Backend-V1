package bssm.bsmauth.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendAuthCodeMailDto {

    private int enrolledAt;
    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
}
