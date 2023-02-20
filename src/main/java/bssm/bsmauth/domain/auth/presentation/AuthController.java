package bssm.bsmauth.domain.auth.presentation;

import bssm.bsmauth.domain.auth.presentation.dto.req.*;
import bssm.bsmauth.domain.auth.presentation.dto.req.teacher.TeacherFindIdMailReq;
import bssm.bsmauth.domain.auth.service.AuthService;
import bssm.bsmauth.domain.user.presentation.dto.req.student.FindStudentReq;
import bssm.bsmauth.domain.auth.presentation.dto.req.teacher.TeacherAuthCodeMailReq;
import bssm.bsmauth.domain.auth.presentation.dto.req.teacher.TeacherSignUpReq;
import bssm.bsmauth.domain.auth.presentation.dto.res.ResetPwTokenRes;
import bssm.bsmauth.domain.auth.presentation.dto.res.AuthTokenRes;
import bssm.bsmauth.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CurrentUser currentUser;

    @PostMapping("login")
    public AuthTokenRes login(@Valid @RequestBody LoginReq req, HttpServletResponse res) throws Exception {
        return authService.loginPostProcess(res, authService.login(req));
    }

    @DeleteMapping("logout")
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        authService.logout(req, res);
    }

    @PostMapping("student")
    public void studentSignUp(@Valid @RequestBody UserSignUpReq req) throws Exception {
        authService.studentSignUp(req);
    }

    @PostMapping("teacher")
    public void teacherSignUp(@Valid @RequestBody TeacherSignUpReq req) throws Exception {
        authService.teacherSignUp(req);
    }

    @PutMapping("pw")
    public void updatePw(@Valid @RequestBody UpdatePwReq req) throws Exception {
        authService.updatePw(currentUser.getUser(), req);
    }

    @PostMapping("pw/token")
    public void resetPwByToken(@Valid @RequestBody ResetPwByTokenReq req) throws Exception {
        authService.resetPwByToken(req);
    }

    @GetMapping("pw/token")
    public ResetPwTokenRes findResetPwTokenInfo(@Valid @RequestParam String token) throws Exception {
        return authService.findResetPwTokenInfo(token);
    }

}
