package bssm.bsmauth.domain.auth.service;

import bssm.bsmauth.domain.auth.domain.TeacherAuthCode;
import bssm.bsmauth.domain.auth.domain.UserToken;
import bssm.bsmauth.domain.auth.domain.UserTokenType;
import bssm.bsmauth.domain.auth.domain.repository.RefreshTokenRepository;
import bssm.bsmauth.domain.auth.domain.repository.TeacherAuthCodeRepository;
import bssm.bsmauth.domain.auth.domain.repository.TokenRepository;
import bssm.bsmauth.domain.auth.exception.AlreadyUsedAuthCodeException;
import bssm.bsmauth.domain.auth.exception.NoSuchAuthCodeException;
import bssm.bsmauth.domain.auth.exception.NoSuchTokenException;
import bssm.bsmauth.domain.auth.presentation.dto.req.*;
import bssm.bsmauth.domain.user.domain.*;
import bssm.bsmauth.domain.user.domain.repository.*;
import bssm.bsmauth.domain.auth.presentation.dto.req.teacher.TeacherSignUpReq;
import bssm.bsmauth.domain.auth.presentation.dto.res.ResetPwTokenRes;
import bssm.bsmauth.domain.auth.presentation.dto.res.AuthTokenRes;
import bssm.bsmauth.domain.user.exception.NoSuchUserException;
import bssm.bsmauth.global.error.exceptions.BadRequestException;
import bssm.bsmauth.global.error.exceptions.ConflictException;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import bssm.bsmauth.global.jwt.JwtProvider;
import bssm.bsmauth.global.cookie.CookieProvider;
import bssm.bsmauth.global.utils.SecurityUtil;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final CookieProvider cookieProvider;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TokenRepository tokenRepository;
    private final TeacherAuthCodeRepository teacherAuthCodeRepository;

    @Value("${env.cookie.name.token}")
    private String TOKEN_COOKIE_NAME;
    @Value("${env.cookie.name.refreshToken}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${env.jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${env.jwt.time.refreshToken}")
    private long JWT_REFRESH_TOKEN_MAX_TIME;

    @Transactional
    public void studentSignUp(UserSignUpReq req) throws Exception {
        Student student = studentRepository.findByAuthCode(req.getAuthCode())
                .orElseThrow(NoSuchAuthCodeException::new);
        if (!student.isCodeAvailable()) {
            throw new AlreadyUsedAuthCodeException();
        }
        validateSignUp(req);

        User user = User.createStudent(student, req.getId(), req.getPw(), req.getNickname());
        student.expireAuthCode();
        userRepository.save(user);
    }

    @Transactional
    public void teacherSignUp(TeacherSignUpReq req) throws Exception {
        TeacherAuthCode teacherAuthCode = teacherAuthCodeRepository.findByTokenAndType(req.getAuthCode(), UserTokenType.AUTH_CODE)
                .orElseThrow(NoSuchAuthCodeException::new);
        if (teacherAuthCode.isUsed()) {
            throw new AlreadyUsedAuthCodeException();
        }
        validateSignUp(req);

        Teacher teacher = teacherRepository.save(Teacher.create(teacherAuthCode.getEmail(), req.getName()));
        User user = User.createTeacher(teacher, req.getId(), req.getPw(), req.getNickname());
        teacherAuthCode.expire();
        userRepository.save(user);
    }

    private void validateSignUp(UserSignUpReq req) {
        validatePw(req.getPw(), req.getCheckPw());
        userRepository.findById(req.getId())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 ID 입니다");});
        userRepository.findByNickname(req.getNickname())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 닉네임 입니다");});
    }

    private void validatePw(String pw, String checkPw) {
        if (!Objects.equals(pw, checkPw)) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("pwCheck", "비밀번호 재입력이 맞지 않습니다").
                    build()
            );
        }
    }

    public User login(LoginReq req) throws Exception {
        User user = userRepository.findById(req.getId())
                .orElseThrow(() -> {
                    throw new BadRequestException(ImmutableMap.<String, String>builder().
                            put("idOrPw", "id 또는 password가 맞지 않습니다").
                            build()
                    );
                });
        if (!user.getPw().equals(SecurityUtil.encryptPw(user.getPwSalt(), req.getPw()))) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("idOrPw", "id 또는 password가 맞지 않습니다").
                    build()
            );
        }
        return user;
    }

    public AuthTokenRes loginPostProcess(HttpServletResponse res, User user) {
        String token = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user.getCode());

        ResponseCookie tokenCookie = cookieProvider.createCookie(TOKEN_COOKIE_NAME, token, JWT_TOKEN_MAX_TIME);
        ResponseCookie refreshTokenCookie = cookieProvider.createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, JWT_REFRESH_TOKEN_MAX_TIME);
        res.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        res.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return AuthTokenRes.create(token, refreshToken);
    }

    @Transactional
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        Cookie refreshTokenCookie = cookieProvider.findCookie(req, REFRESH_TOKEN_COOKIE_NAME);
        expireRefreshToken(refreshTokenCookie);

        res.addHeader(HttpHeaders.SET_COOKIE, cookieProvider.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0).toString());
        res.addHeader(HttpHeaders.SET_COOKIE, cookieProvider.createCookie(TOKEN_COOKIE_NAME, "", 0).toString());
    }

    private void expireRefreshToken(Cookie refreshTokenCookie) {
        if (refreshTokenCookie == null) return;

        try {
            String refreshToken = jwtProvider.getRefreshToken(refreshTokenCookie.getValue());
            refreshTokenRepository.findById(refreshToken)
                    .ifPresent(token -> token.setAvailable(false));
        } catch (Exception ignored) {}
    }


    @Transactional
    public void updatePw(User user, UpdatePwReq req) {
        validatePw(req.getNewPw(), req.getCheckNewPw());
        User newUser = userRepository.findById(user.getCode())
                .orElseThrow(NoSuchUserException::new);
        newUser.updatePw(req.getNewPw());
    }

    @Transactional
    public void resetPwByToken(ResetPwByTokenReq req) {
        UserToken token = tokenRepository.findByTokenAndType(req.getToken(), UserTokenType.RESET_PW)
                .orElseThrow(NoSuchTokenException::new);
        if (token.isUsed() || new Date().after(token.getExpireIn())) throw new NoSuchTokenException();
        validatePw(req.getNewPw(), req.getCheckNewPw());

        User user = token.getUser();
        user.updatePw(req.getNewPw());
        token.expire();
    }

    public ResetPwTokenRes findResetPwTokenInfo(String token) {
        UserToken tokenInfo = tokenRepository.findByTokenAndType(token, UserTokenType.RESET_PW)
                .orElseThrow(NoSuchTokenException::new);
        return ResetPwTokenRes.builder()
                .used(tokenInfo.isUsed())
                .expireIn(tokenInfo.getExpireIn())
                .build();
    }
}
