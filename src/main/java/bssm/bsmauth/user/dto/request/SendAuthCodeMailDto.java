package bssm.bsmauth.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class SendAuthCodeMailDto {

    private int enrolledAt;
    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
}
