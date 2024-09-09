package bssm.bsmauth.domain.auth.presentation;

import bssm.bsmauth.domain.auth.presentation.dto.req.*;
import bssm.bsmauth.domain.auth.service.AuthService;
import bssm.bsmauth.domain.auth.presentation.dto.req.teacher.TeacherSignUpReq;
import bssm.bsmauth.domain.auth.presentation.dto.res.ResetPwTokenRes;
import bssm.bsmauth.domain.auth.presentation.dto.res.AuthTokenRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public AuthTokenRes login(@Valid @RequestBody LoginReq req, HttpServletRequest rawReq, HttpServletResponse rawRes) throws IOException {
        return authService.loginPostProcess(rawRes, authService.login(rawReq, req));
    }

    @DeleteMapping("logout")
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        authService.logout(req, res);
    }

    @PostMapping("student")
    public void studentSignUp(@Valid @RequestBody UserSignUpReq req) {
        authService.studentSignUp(req);
    }

    @PostMapping("teacher")
    public void teacherSignUp(@Valid @RequestBody TeacherSignUpReq req) {
        authService.teacherSignUp(req);
    }

    @PutMapping("pw")
    public void updatePw(@Valid @RequestBody UpdatePwReq req) {
        authService.updatePw(req);
    }

    @PostMapping("pw/token")
    public void resetPwByToken(@Valid @RequestBody ResetPwByTokenReq req) {
        authService.resetPwByToken(req);
    }

    @GetMapping("pw/token")
    public ResetPwTokenRes findResetPwTokenInfo(@Valid @RequestParam String token) {
        return authService.findResetPwTokenInfo(token);
    }

}
