package bssm.bsmauth.domain.user.presentation;

import bssm.bsmauth.domain.user.presentation.dto.response.*;
import bssm.bsmauth.domain.user.service.UserService;
import bssm.bsmauth.domain.user.presentation.dto.request.*;
import bssm.bsmauth.domain.user.presentation.dto.request.student.FindStudentRequest;
import bssm.bsmauth.domain.user.presentation.dto.request.teacher.TeacherEmailDto;
import bssm.bsmauth.domain.user.presentation.dto.request.teacher.TeacherSignUpRequest;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.utils.CookieUtil;
import bssm.bsmauth.global.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserUtil userUtil;

    @Value("${env.cookie.name.token}")
    private String TOKEN_COOKIE_NAME;
    @Value("${env.cookie.name.refreshToken}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${env.jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${env.jwt.time.refreshToken}")
    private long JWT_REFRESH_TOKEN_MAX_TIME;

    @GetMapping()
    public UserResponse getUserInfo() {
        return userUtil.getUser().toUserResponse();
    }

    @GetMapping("{userCode}")
    public OtherUserResponse getOtherUserInfo(@PathVariable long userCode) {
        return userService.getOtherUserInfo(userCode);
    }

    @DeleteMapping("logout")
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        userService.logout(req, res);
    }

    @PostMapping("student")
    public void studentSignUp(@Valid @RequestBody UserSignUpRequest dto) throws Exception {
        userService.studentSignUp(dto);
    }

    @PostMapping("teacher")
    public void teacherSignUp(@Valid @RequestBody TeacherSignUpRequest dto) throws Exception {
        userService.teacherSignUp(dto);
    }

    @PostMapping("login")
    public UserLoginResponse login(@Valid @RequestBody LoginRequest dto, HttpServletResponse res) throws Exception {
        return userService.loginPostProcess(res, userService.login(dto));
    }

    @PutMapping("pw")
    public void updatePw(@Valid @RequestBody UpdatePwRequest dto) throws Exception {
        userService.updatePw(userUtil.getUser(), dto);
    }

    @PostMapping("pw/token")
    public void resetPwByToken(@Valid @RequestBody ResetPwByTokenRequest dto) throws Exception {
        userService.resetPwByToken(dto);
    }

    @GetMapping("pw/token")
    public ResetPwTokenResponse getResetPwTokenInfo(@Valid @RequestParam String token) throws Exception {
        return userService.getResetPwTokenInfo(token);
    }

    @PutMapping("nickname")
    public void updateNickname(@Valid @RequestBody UpdateNicknameRequest dto, HttpServletResponse res) throws Exception {
        userService.updateNickname(userUtil.getUser(), dto);
    }

    @PostMapping("profile")
    public void uploadProfile(@RequestParam MultipartFile file) {
        userService.uploadProfile(userUtil.getUser(), file);
    }

    @PostMapping("mail/authcode/student")
    public void sendAuthCodeMail(@Valid @RequestBody FindStudentRequest dto) {
        userService.studentAuthCodeMail(dto);
    }

    @PostMapping("mail/authcode/teacher")
    public void sendAuthCodeMail(@Valid @RequestBody TeacherEmailDto dto) {
        userService.teacherAuthCodeMail(dto);
    }

    @PostMapping("mail/id/student")
    public void studentFindIdMail(@Valid @RequestBody FindStudentRequest dto) {
        userService.studentFindIdMail(dto);
    }

    @PostMapping("mail/id/teacher")
    public void teacherFindIdMail(@Valid @RequestBody TeacherEmailDto dto) {
        userService.teacherFindIdMail(dto);
    }

    @PostMapping("mail/pw")
    public void sendResetPwMail(@Valid @RequestBody SendResetPwMailRequest dto) {
        userService.resetPwMail(dto);
    }
}
