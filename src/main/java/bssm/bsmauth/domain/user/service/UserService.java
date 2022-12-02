package bssm.bsmauth.domain.user.service;

import bssm.bsmauth.domain.user.domain.*;
import bssm.bsmauth.domain.user.domain.repository.*;
import bssm.bsmauth.domain.user.facade.UserFacade;
import bssm.bsmauth.domain.user.presentation.dto.request.*;
import bssm.bsmauth.domain.user.presentation.dto.request.student.FindStudentRequest;
import bssm.bsmauth.domain.user.presentation.dto.request.teacher.TeacherEmailDto;
import bssm.bsmauth.domain.user.presentation.dto.request.teacher.TeacherSignUpRequest;
import bssm.bsmauth.domain.user.presentation.dto.response.OtherUserResponse;
import bssm.bsmauth.domain.user.domain.UserTokenType;
import bssm.bsmauth.domain.user.domain.UserRole;
import bssm.bsmauth.domain.user.presentation.dto.response.UserLoginResponse;
import bssm.bsmauth.domain.user.presentation.dto.response.UserResponse;
import bssm.bsmauth.global.error.exceptions.BadRequestException;
import bssm.bsmauth.global.error.exceptions.ConflictException;
import bssm.bsmauth.global.error.exceptions.InternalServerException;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import bssm.bsmauth.domain.user.presentation.dto.response.ResetPwTokenResponse;
import bssm.bsmauth.global.jwt.JwtProvider;
import bssm.bsmauth.global.utils.CookieUtil;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HexFormat;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFacade userFacade;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TokenRepository tokenRepository;
    private final TeacherAuthCodeRepository teacherAuthCodeRepository;
    private final UserMailProvider userMailService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @Value("${env.cookie.name.token}")
    private String TOKEN_COOKIE_NAME;
    @Value("${env.cookie.name.refreshToken}")
    private String REFRESH_TOKEN_COOKIE_NAME;
    @Value("${env.jwt.time.token}")
    private long JWT_TOKEN_MAX_TIME;
    @Value("${env.jwt.time.refreshToken}")
    private long JWT_REFRESH_TOKEN_MAX_TIME;

    @Value("${env.file.path.base}")
    private String PUBLIC_RESOURCE_PATH;
    @Value("${env.file.path.upload.profile}")
    private String PROFILE_UPLOAD_RESOURCE_PATH;

    public UserResponse getUserInfo(long userCode) {
        return userRepository.findById(userCode)
                .orElseThrow(NotFoundException::new)
                .toUserResponse();
    }

    public OtherUserResponse getOtherUserInfo(long userCode) {
        return userRepository.findById(userCode)
                .orElseThrow(NotFoundException::new)
                .toOtherUserResponse();
    }

    @Transactional
    public void studentSignUp(UserSignUpRequest dto) throws Exception {
        Student studentInfo = studentRepository.findByAuthCode(dto.getAuthCode())
                .orElseThrow(() -> {throw new NotFoundException("인증코드를 찾을 수 없습니다");});
        if (!studentInfo.isCodeAvailable()) {
            Object String;
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("authCode", "이미 사용된 인증코드입니다").
                    build()
            );
        }
        User user = signUp(dto)
                .studentId(studentInfo.getStudentId())
                .role(UserRole.STUDENT)
                .build();

        studentInfo.setCodeAvailable(false);
        userRepository.save(user);
    }

    @Transactional
    public void teacherSignUp(TeacherSignUpRequest dto) throws Exception {
        TeacherAuthCode teacherAuthCode = teacherAuthCodeRepository.findByTokenAndType(dto.getAuthCode(), UserTokenType.AUTH_CODE)
                .orElseThrow(() -> {throw new NotFoundException("인증코드를 찾을 수 없습니다");});
        if (teacherAuthCode.isUsed()) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("authCode", "이미 사용된 인증코드입니다").
                    build()
            );
        }
        Teacher teacher = teacherRepository.save(
                Teacher.builder()
                        .email(teacherAuthCode.getEmail())
                        .name(dto.getName())
                        .build()
        );
        User user = signUp(dto)
                .teacherId(teacher.getTeacherId())
                .role(UserRole.TEACHER)
                .build();

        teacherAuthCodeRepository.delete(teacherAuthCode);
        userRepository.save(user);
    }

    private User.UserBuilder signUp(UserSignUpRequest dto) throws Exception {
        if (!dto.getPw().equals(dto.getCheckPw())) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("pwCheck", "비밀번호 재입력이 맞지 않습니다").
                    build()
            );
        }
        userRepository.findById(dto.getId())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 ID 입니다");});
        userRepository.findByNickname(dto.getNickname())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 닉네임 입니다");});

        // 비밀번호 솔트 값 생성
        String salt = getRandomStr(64);
        // 비밀번호 암호화
        String encryptedPw = encryptPw(salt, dto.getPw());

        return User.builder()
                .id(dto.getId())
                .pw(encryptedPw)
                .pwSalt(salt)
                .nickname(dto.getNickname());
    }

    public User login(LoginRequest dto) throws Exception {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> {
                    throw new BadRequestException(ImmutableMap.<String, String>builder().
                            put("idOrPw", "id 또는 password가 맞지 않습니다").
                            build()
                    );
                });
        if (!user.getPw().equals(encryptPw(user.getPwSalt(), dto.getPw()))) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("idOrPw", "id 또는 password가 맞지 않습니다").
                    build()
            );
        }
        return user;
    }

    public UserLoginResponse loginPostProcess(HttpServletResponse res, User user) {
        String token = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user.getCode());

        Cookie tokenCookie = cookieUtil.createCookie(TOKEN_COOKIE_NAME, token, JWT_TOKEN_MAX_TIME);
        Cookie refreshTokenCookie = cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, JWT_REFRESH_TOKEN_MAX_TIME);
        res.addCookie(tokenCookie);
        res.addCookie(refreshTokenCookie);

        return UserLoginResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        Cookie refreshTokenCookie = cookieUtil.getCookie(req, REFRESH_TOKEN_COOKIE_NAME);
        if (refreshTokenCookie != null) {
            try {
                refreshTokenRepository.findById(
                        jwtProvider.getRefreshToken(refreshTokenCookie.getValue())
                ).ifPresent(token -> token.setAvailable(false));
            } catch (Exception ignored) {}
        }

        res.addCookie(cookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0));
        res.addCookie(cookieUtil.createCookie(TOKEN_COOKIE_NAME, "", 0));
    }

    public void updatePw(User user, UpdatePwRequest dto) throws Exception {
        if (!dto.getNewPw().equals(dto.getCheckNewPw())) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("pwCheck", "비밀번호 재입력이 맞지 않습니다").
                    build()
            );
        }
        User newUser = userRepository.findById(user.getCode()).orElseThrow(
                () -> {throw new NotFoundException("유저를 찾을 수 없습니다");}
        );

        // 새 비밀번호 솔트 값 생성
        String newSalt = getRandomStr(64);
        // 새 비밀번호 암호화
        String newPw = encryptPw(newSalt, dto.getNewPw());

        newUser.setPw(newPw);
        newUser.setPwSalt(newSalt);
        userRepository.save(newUser);
    }

    public void resetPwByToken(ResetPwByTokenRequest dto) throws Exception {
        UserToken token = tokenRepository.findByTokenAndType(dto.getToken(), UserTokenType.RESET_PW).orElseThrow(
                () -> {throw new NotFoundException("토큰을 찾을 수 없습니다");}
        );
        if (token.isUsed() || new Date().after(token.getExpireIn())) throw new NotFoundException("토큰이 만료되었습니다");

        updatePw(token.getUser(), dto);
        token.setUsed(true);
        tokenRepository.save(token);
    }

    public ResetPwTokenResponse getResetPwTokenInfo(String token) {
        UserToken tokenInfo = tokenRepository.findByTokenAndType(token, UserTokenType.RESET_PW).orElseThrow(
                () -> {throw new NotFoundException("토큰을 찾을 수 없습니다");}
        );
        return ResetPwTokenResponse.builder()
                .used(tokenInfo.isUsed())
                .expireIn(tokenInfo.getExpireIn())
                .build();
    }

    public void updateNickname(User user, UpdateNicknameRequest dto) {
        userRepository.findByNickname(dto.getNewNickname())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 닉네임 입니다");});
        User newUser = userRepository.findById(user.getCode()).orElseThrow(
                () -> {throw new NotFoundException("유저를 찾을 수 없습니다");}
        );

        newUser.setNickname(dto.getNewNickname());
        userRepository.save(newUser);
        userFacade.saveCacheUser(newUser);
    }

    public void uploadProfile(User user, MultipartFile file) {
        String fileExt = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String fileId = String.valueOf(user.getCode());

        File dir = new File(PUBLIC_RESOURCE_PATH + PROFILE_UPLOAD_RESOURCE_PATH);
        File newFile = new File(dir.getPath() + "/" + fileId + "." + fileExt);

        if (!dir.exists()) dir.mkdirs();
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("프로필 사진 업로드에 실패하였습니다");
        }

        int newImageSize = 128;
        try {
            BufferedImage image = ImageIO.read(newFile);
            BufferedImage newImage = new BufferedImage(newImageSize, newImageSize, BufferedImage.TYPE_INT_RGB);

            Image resizedImage = image.getScaledInstance(newImageSize, newImageSize, Image.SCALE_REPLICATE);
            newImage.createGraphics().drawImage(resizedImage, 0, 0, Color.white, null);

            newFile.renameTo(new File(dir.getPath() + "/" + fileId + ".png"));
            ImageIO.write(newImage, "png", newFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("프로필 사진 변환에 실패하였습니다");
        }
    }

    public void studentAuthCodeMail(FindStudentRequest dto) {
        Student student = studentRepository.findByGradeAndClassNoAndStudentNoAndName(
                dto.getGrade(),
                dto.getClassNo(),
                dto.getStudentNo(),
                dto.getName()
        ).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );

        userMailService.sendAuthCodeMail(student.getEmail(), student.getAuthCode());
    }

    @Transactional
    public void teacherAuthCodeMail(TeacherEmailDto dto) {
        if (!Pattern.matches("teacher\\d.*@bssm\\.hs\\.kr", dto.getEmail())) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("email", "올바른 선생님 이메일 주소가 아닙니다").
                    build()
            );
        }
        if (teacherRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("해당하는 이메일의 계정이 이미 존재합니다");
        }
        teacherAuthCodeRepository.deleteByEmail(dto.getEmail());

        Date expireIn = new Date();
        expireIn.setTime(expireIn.getTime() + (5 * 60 * 1000));

        TeacherAuthCode authCode = TeacherAuthCode.builder()
                .token(getRandomStr(6))
                .email(dto.getEmail())
                .used(false)
                .type(UserTokenType.AUTH_CODE)
                .expireIn(expireIn)
                .build();
        teacherAuthCodeRepository.save(authCode);

        userMailService.sendAuthCodeMail(dto.getEmail(), authCode.getToken());
    }

    public void studentFindIdMail(FindStudentRequest dto) {
        Student student = studentRepository.findByGradeAndClassNoAndStudentNoAndName(
                dto.getGrade(),
                dto.getClassNo(),
                dto.getStudentNo(),
                dto.getName()
        ).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );

        User user = userRepository.findByStudent(student).orElseThrow(
                () -> {throw new NotFoundException("없는 유저입니다, 먼저 회원가입을 해주세요");}
        );

        userMailService.sendFindIdMail(student.getEmail(), user.getId());
    }

    public void teacherFindIdMail(TeacherEmailDto dto) {
        User user = userRepository.findByRoleAndTeacherEmail(UserRole.TEACHER, dto.getEmail()).orElseThrow(
                () -> {throw new NotFoundException("계정을 찾을 수 없습니다");}
        );

        userMailService.sendFindIdMail(user.getTeacher().getEmail(), user.getId());
    }

    public void resetPwMail(SendResetPwMailRequest dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow(
                () -> {throw new NotFoundException("없는 유저입니다, 먼저 회원가입을 해주세요");}
        );

        Date expireIn = new Date();
        expireIn.setTime(expireIn.getTime() + (5 * 60 * 1000));

        UserToken token = UserToken.builder()
                .token(getRandomStr(32))
                .usercode(user.getCode())
                .used(false)
                .type(UserTokenType.RESET_PW)
                .expireIn(expireIn)
                .build();

        String email = user.getRole() == UserRole.TEACHER? user.getTeacher().getEmail(): user.getStudent().getEmail();
        userMailService.sendResetPwMail(email, token.getToken());
        tokenRepository.save(token);
    }

    private String getRandomStr(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length / 2];
        secureRandom.nextBytes(randomBytes);
        return HexFormat.of().formatHex(randomBytes);
    }

    private String encryptPw(String salt, String pw) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA3-256");
        messageDigest.update((salt + pw).getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(messageDigest.digest());
    }
}
