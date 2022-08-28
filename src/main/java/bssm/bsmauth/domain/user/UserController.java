package bssm.bsmauth.domain.user;

import bssm.bsmauth.domain.user.dto.request.*;
import bssm.bsmauth.domain.user.dto.request.student.FindStudentDto;
import bssm.bsmauth.domain.user.dto.request.teacher.SendTeacherAuthCodeMailDto;
import bssm.bsmauth.domain.user.dto.request.teacher.TeacherSignUpDto;
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
    public void studentSignUp(@RequestBody UserSignUpDto dto) throws Exception {
        userService.studentSignUp(dto);
    }

    @PostMapping("teacher")
    public void teacherSignUp(@RequestBody TeacherSignUpDto dto) throws Exception {
        userService.teacherSignUp(dto);
    }

    @PostMapping("login")
    public UserLoginResponseDto login(@RequestBody UserLoginDto dto, HttpServletResponse res) throws Exception {
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
    public void updatePw(@RequestBody UserUpdatePwDto dto) throws Exception {
        userService.updatePw(userUtil.getCurrentUser(), dto);
    }

    @PostMapping("pw/token")
    public void resetPwByToken(@RequestBody UserResetPwByTokenDto dto) throws Exception {
        userService.resetPwByToken(dto);
    }

    @GetMapping("pw/token")
    public ResetPwTokenInfoDto getResetPwTokenInfo(@RequestParam String token) throws Exception {
        return userService.getResetPwTokenInfo(token);
    }

    @PutMapping("nickname")
    public UserUpdateNicknameResponseDto updateNickname(@RequestBody UserUpdateNicknameDto dto, HttpServletResponse res) throws Exception {
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
    public void sendAuthCodeMail(@RequestBody FindStudentDto dto) {
        userService.studentAuthCodeMail(dto);
    }

    @PostMapping("mail/authcode/teacher")
    public void sendAuthCodeMail(@RequestBody SendTeacherAuthCodeMailDto dto) {
        userService.teacherAuthCodeMail(dto);
    }

    @PostMapping("mail/id/student")
    public void sendFindIdMail(@RequestBody FindStudentDto dto) {
        userService.studentFindIdMail(dto);
    }

    @PostMapping("mail/pw")
    public void sendResetPwMail(@RequestBody SendResetPwMailDto dto) {
        userService.resetPwMail(dto);
    }
}
