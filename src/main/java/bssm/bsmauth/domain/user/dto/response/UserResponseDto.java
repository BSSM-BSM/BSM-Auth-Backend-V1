package bssm.bsmauth.domain.user.dto.response;

import bssm.bsmauth.domain.user.dto.response.student.StudentResponseDto;
import bssm.bsmauth.domain.user.dto.response.teacher.TeacherResponseDto;
import bssm.bsmauth.domain.user.type.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    private Long code;
    private String nickname;
    private UserRole role;
    private StudentResponseDto student;
    private TeacherResponseDto teacher;
}
