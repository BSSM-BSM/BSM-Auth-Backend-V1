package bssm.bsmauth.domain.user.dto.request.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class FindStudentDto {

    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
}
