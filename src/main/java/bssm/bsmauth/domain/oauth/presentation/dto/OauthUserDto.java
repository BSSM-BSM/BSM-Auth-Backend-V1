package bssm.bsmauth.domain.oauth.presentation.dto;

import bssm.bsmauth.domain.oauth.domain.OauthScope;
import bssm.bsmauth.domain.user.domain.Student;
import bssm.bsmauth.domain.user.domain.Teacher;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.type.UserRole;
import bssm.bsmauth.global.error.exceptions.InternalServerException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OauthUserDto {

    /** @deprecated id로 변경되었지만 하위호환 유지 필요 */
    @Deprecated
    private Long code;
    private Long id;
    private String nickname;
    private Boolean isGraduate;
    private Integer enrolledAt;
    private Integer cardinal;
    private Integer grade;
    private Integer classNo;
    private Integer studentNo;
    private String name;
    private String email;
    private UserRole role;
    private String profileUrl;

    public static OauthUserDto create(List<OauthScope> scopeList, User user) {
        OauthUserDto dto = new OauthUserDto();
        dto.role = user.getRole();
        dto.profileUrl = user.getProfileUrl();
        scopeList.forEach(scope -> dto.setData(scope, user));
        return dto;
    }

    public void setData(OauthScope scope, User user) {
        switch (scope.getId()) {
            case "code" -> {
                this.code = user.getId();
                this.id = user.getId();
            }
            case "nickname" -> this.nickname = user.getNickname();
            case "name" -> this.name = user.findNameOrNull();
            case "email" -> this.email = user.findEmailOrNull();
            default -> {
                if (this.role == UserRole.STUDENT) setStudent(scope, user.getStudent());
                else if (this.role == UserRole.TEACHER) setTeacher(scope, user.getTeacher());
                else throw new InternalServerException();
            }
        }

    }

    public void setStudent(OauthScope scope, Student student) {
        switch (scope.getId()) {
            case "isGraduate" -> this.isGraduate = student.isGraduate();
            case "enrolledAt" -> {
                this.enrolledAt = student.getEnrolledAt();
                this.cardinal = student.getCardinal();
            }
            case "grade" -> this.grade = student.getGrade();
            case "classNo" -> this.classNo = student.getClassNo();
            case "studentNo" -> this.studentNo = student.getStudentNo();
        }
    }

    // 선생님 관련 데이터가 필요하면 사용 예정
    public void setTeacher(OauthScope scope, Teacher teacher) {
//        switch (scope.getId()) {
//            default -> {}
//        }
    }
}
