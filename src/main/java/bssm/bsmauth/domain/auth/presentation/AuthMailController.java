package bssm.bsmauth.domain.auth.presentation;

import bssm.bsmauth.domain.auth.presentation.dto.req.*;
import bssm.bsmauth.domain.auth.presentation.dto.req.teacher.TeacherAuthCodeMailReq;
import bssm.bsmauth.domain.auth.presentation.dto.req.teacher.TeacherFindIdMailReq;
import bssm.bsmauth.domain.auth.service.AuthMailService;
import bssm.bsmauth.domain.user.presentation.dto.req.student.FindStudentReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("auth/mail")
@RequiredArgsConstructor
public class AuthMailController {

    private final AuthMailService authMailService;

    @PostMapping("authcode/student")
    public void sendAuthCodeMail(@Valid @RequestBody FindStudentReq req) {
        authMailService.studentAuthCodeMail(req);
    }

    @PostMapping("authcode/teacher")
    public void sendAuthCodeMail(@Valid @RequestBody TeacherAuthCodeMailReq req) {
        authMailService.teacherAuthCodeMail(req);
    }

    @PostMapping("id/student")
    public void studentFindIdMail(@Valid @RequestBody FindStudentReq req) {
        authMailService.studentFindIdMail(req);
    }

    @PostMapping("id/teacher")
    public void teacherFindIdMail(@Valid @RequestBody TeacherFindIdMailReq req) {
        authMailService.teacherFindIdMail(req);
    }

    @PostMapping("pw")
    public void sendResetPwMail(@Valid @RequestBody ResetPwMailReq dto) {
        authMailService.resetPwMail(dto);
    }
}
