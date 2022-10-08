package bssm.bsmauth.domain.user;

import bssm.bsmauth.domain.user.dto.request.*;
import bssm.bsmauth.domain.user.dto.request.student.FindStudentRequest;
import bssm.bsmauth.domain.user.dto.request.teacher.TeacherEmailDto;
import bssm.bsmauth.domain.user.dto.request.teacher.TeacherSignUpRequest;
import bssm.bsmauth.domain.user.dto.response.UserLoginResponseDto;
import bssm.bsmauth.domain.user.dto.response.UserResponseDto;
import bssm.bsmauth.domain.user.dto.response.UserUpdateNicknameResponseDto;
import bssm.bsmauth.domain.user.entities.User;
import bssm.bsmauth.global.utils.CookieUtil;
import bssm.bsmauth.global.utils.JwtUtil;
import bssm.bsmauth.global.utils.UserUtil;
import bssm.bsmauth.domain.user.dto.response.ResetPwTokenInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
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
    public UserResponseDto getUserInfo() {
        return userService.userInfo(userUtil.getCurrentUser());
    }

    @DeleteMapping("logout")
    public void logout(HttpServletResponse res) {
        res.addCookie(cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0));
        res.addCookie(cookieUtil.createCookie(TOKEN_COOKIE_NAME, "", 0));
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
    public UserLoginResponseDto login(@Valid @RequestBody LoginRequest dto, HttpServletResponse res) throws Exception {
        User user = userService.login(dto);

        String token = jwtUtil.createAccessToken(user);
        String refreshToken = jwtUtil.createRefreshToken(user.getCode());
        Cookie tokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, token, JWT_TOKEN_MAX_TIME);
        Cookie refreshTokenCookie = cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, JWT_REFRESH_TOKEN_MAX_TIME);
        res.addCookie(tokenCookie);
        res.addCookie(refreshTokenCookie);

        return UserLoginResponseDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    @PutMapping("pw")
    public void updatePw(@Valid @RequestBody UpdatePwRequest dto) throws Exception {
        userService.updatePw(userUtil.getCurrentUser(), dto);
    }

    @PostMapping("pw/token")
    public void resetPwByToken(@Valid @RequestBody ResetPwByTokenRequest dto) throws Exception {
        userService.resetPwByToken(dto);
    }

    @GetMapping("pw/token")
    public ResetPwTokenInfoDto getResetPwTokenInfo(@Valid @RequestParam String token) throws Exception {
        return userService.getResetPwTokenInfo(token);
    }

    @PutMapping("nickname")
    public UserUpdateNicknameResponseDto updateNickname(@Valid @RequestBody UpdateNicknameRequest dto, HttpServletResponse res) throws Exception {
        User user = userService.updateNickname(userUtil.getCurrentUser(), dto);

        String token = jwtUtil.createAccessToken(user);
        Cookie tokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, token, JWT_TOKEN_MAX_TIME);
        res.addCookie(tokenCookie);

        return new UserUpdateNicknameResponseDto(token);
    }

    @PostMapping("profile")
    public void uploadProfile(@RequestParam MultipartFile file) {
        userService.uploadProfile(userUtil.getCurrentUser(), file);
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
