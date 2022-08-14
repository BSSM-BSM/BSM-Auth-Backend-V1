package bssm.bsmauth.user;

import bssm.bsmauth.global.exceptions.BadRequestException;
import bssm.bsmauth.global.exceptions.ConflictException;
import bssm.bsmauth.global.exceptions.InternalServerException;
import bssm.bsmauth.global.exceptions.NotFoundException;
import bssm.bsmauth.global.mail.MailService;
import bssm.bsmauth.global.mail.dto.MailDto;
import bssm.bsmauth.user.dto.request.*;
import bssm.bsmauth.user.entities.ResetPwToken;
import bssm.bsmauth.user.entities.Student;
import bssm.bsmauth.user.entities.User;
import bssm.bsmauth.user.repositories.ResetPwTokenRepository;
import bssm.bsmauth.user.repositories.StudentRepository;
import bssm.bsmauth.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final MailService mailService;
    private final ResetPwTokenRepository resetPwTokenRepository;
    @Value("${PUBLIC_RESOURCE_PATH}")
    private String PUBLIC_RESOURCE_PATH;
    @Value("${PROFILE_UPLOAD_RESOURCE_PATH}")
    private String PROFILE_UPLOAD_RESOURCE_PATH;

    public User userInfo(int usercode) {
        User user = userRepository.findById(usercode).orElseThrow(
                () -> {throw new NotFoundException("유저를 찾을 수 없습니다");}
        );

        Student studentInfo = Student.builder()
                .enrolledAt(user.getStudent().getEnrolledAt())
                .grade(user.getStudent().getGrade())
                .classNo(user.getStudent().getClassNo())
                .studentNo(user.getStudent().getStudentNo())
                .name(user.getStudent().getName())
                .build();

        return User.builder()
                .usercode(user.getUsercode())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .student(studentInfo)
                .build();
    }

    @Transactional
    public User signUp(UserSignUpDto dto) throws Exception {
        User user = dto.toEntity();

        if (!dto.getPw().equals(dto.getCheckPw())) {
            throw new BadRequestException("비밀번호 재입력이 맞지 않습니다");
        }

        userRepository.findById(user.getId())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 ID 입니다");});
        userRepository.findByNickname(user.getNickname())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 닉네임 입니다");});

        Student studentInfo = studentRepository.findByAuthCode(dto.getAuthCode())
                .orElseThrow(() -> {throw new NotFoundException("인증코드를 찾을 수 없습니다");});
        if (!studentInfo.isCodeAvailable()) {
            throw new BadRequestException("이미 사용된 인증코드입니다");
        }

        studentInfo.setCodeAvailable(false);
        user.setUniqNo(studentInfo.getUniqNo());
        user.setLevel(studentInfo.getLevel());
        user.setCreatedAt(new Date());

        // 비밀번호 솔트 값 생성
        String salt = getRandomStr(64);
        // 비밀번호 암호화
        String newPw = encryptPw(salt, dto.getPw());

        user.setPwSalt(salt);
        user.setPw(newPw);
        userRepository.save(user);

        return user;
    }

    public User login(UserLoginDto dto) throws Exception {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> {throw new BadRequestException("id 또는 password가 맞지 않습니다");});
        if (!user.getPw().equals(encryptPw(user.getPwSalt(), dto.getPw()))) {
            throw new BadRequestException("id 또는 password가 맞지 않습니다");
        }
        return user;
    }

    public void updatePw(User user, UserUpdatePwDto dto) throws Exception {
        if (!dto.getNewPw().equals(dto.getCheckNewPw())) {
            throw new BadRequestException("비밀번호 재입력이 맞지 않습니다");
        }
        User newUser = userRepository.findById(user.getUsercode()).orElseThrow(
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

    public void resetPwByToken(UserResetPwByTokenDto dto) throws Exception {
        ResetPwToken token = resetPwTokenRepository.findByToken(dto.getToken()).orElseThrow(
                () -> {throw new NotFoundException("토큰을 찾을 수 없습니다");}
        );
        if (!token.isAvailable() || new Date().after(token.getExpireIn())) throw new NotFoundException("토큰이 만료되었습니다");

        updatePw(token.getUser(), dto);
        token.setAvailable(false);
        resetPwTokenRepository.save(token);
    }

    public User updateNickname(User user, UserUpdateNicknameDto dto) {
        userRepository.findByNickname(dto.getNewNickname())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 닉네임 입니다");});
        User newUser = userRepository.findById(user.getUsercode()).orElseThrow(
                () -> {throw new NotFoundException("유저를 찾을 수 없습니다");}
        );

        newUser.setNickname(dto.getNewNickname());
        return userRepository.save(newUser);
    }

    public void uploadProfile(User user, MultipartFile file) {
        if (file.getOriginalFilename() == null) throw new BadRequestException();
        String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String fileId = String.valueOf(user.getUsercode());

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

    public void sendAuthCodeMail(FindStudentDto dto) {
        Student student = studentRepository.findByEnrolledAtAndGradeAndClassNoAndStudentNoAndName(
                dto.getEnrolledAt(),
                dto.getGrade(),
                dto.getClassNo(),
                dto.getStudentNo(),
                dto.getName()
        ).orElseThrow(
                () -> {throw new NotFoundException("학생을 찾을 수 없습니다");}
        );

        String content = "<!DOCTYPE HTML>\n" +
                "    <html lang=\"kr\">\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div style=\"display:flex;justify-content:center;\">\n" +
                "            <div style=\"padding:25px 0;text-align:center;margin:0 auto;border:solid 5px;border-radius:25px;font-family:-apple-system,BlinkMacSystemFont,'Malgun Gothic','맑은고딕',helvetica,'Apple SD Gothic Neo',sans-serif;background-color:#202124; color:#e8eaed;\">\n" +
                "                <img src=\"https://bssm.kro.kr/icons/logo.png\" alt=\"로고\" style=\"height:35px; padding-top:12px;\">\n" +
                "                <h1 style=\"font-size:28px;margin-left:25px;margin-right:25px;\">BSM 회원가입 인증 코드입니다.</h1>\n" +
                "                <h2 style=\"display:inline-block;font-size:20px;font-weight:bold;text-align:center;margin:0;color:#e8eaed;padding:15px;border-radius:7px;box-shadow:20px 20px 50px rgba(0, 0, 0, 0.5);background-color:rgba(192, 192, 192, 0.2);\">"+ student.getAuthCode() +"</h2>\n" +
                "                <br><br><br>\n" +
                "                <div style=\"background-color:rgba(192, 192, 192, 0.2);padding:10px;text-align:left;font-size:14px;\">\n" +
                "                    <p style=\"margin:0;\">- 본 이메일은 발신전용 이메일입니다</p>\n" +
                "                    <p style=\"margin:0;\">- 인증 코드는 한 사람당 한 개의 계정에만 쓸 수 있습니다</p>\n" +
                "                </div><br>\n" +
                "                <footer style=\"padding:15px 0;bottom:0;width:100%;font-size:15px;text-align:center;font-weight:bold;\">\n" +
                "                    <p style=\"margin:0;\">부산 소프트웨어 마이스터고 학교 지원 서비스</p>\n" +
                "                    <p style=\"margin:0;\">Copyright 2022. BSM TEAM all rights reserved.</p>\n" +
                "                </footer>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "    </html>";

        MailDto mailDto = MailDto.builder()
                        .to(student.getEmail())
                        .subject("BSM 회원가입 인증 코드입니다")
                        .content(content)
                        .build();
        mailService.sendMail(mailDto);
    }

    public void sendFindIdMail(FindStudentDto dto) {
        Student student = studentRepository.findByEnrolledAtAndGradeAndClassNoAndStudentNoAndName(
                dto.getEnrolledAt(),
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

        String content = "<!DOCTYPE HTML>\n" +
                "    <html lang=\"kr\">\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div style=\"display:flex;justify-content:center;\">\n" +
                "            <div style=\"padding:25px 0;text-align:center;margin:0 auto;border:solid 5px;border-radius:25px;font-family:-apple-system,BlinkMacSystemFont,'Malgun Gothic','맑은고딕',helvetica,'Apple SD Gothic Neo',sans-serif;background-color:#202124; color:#e8eaed;\">\n" +
                "                <img src=\"https://bssm.kro.kr/icons/logo.png\" alt=\"로고\" style=\"height:35px; padding-top:12px;\">\n" +
                "                <h1 style=\"font-size:28px;margin-left:25px;margin-right:25px;\">BSM ID 복구 메일입니다</h1>\n" +
                "                <h2 style=\"display:inline-block;font-size:20px;font-weight:bold;text-align:center;margin:0;color:#e8eaed;padding:15px;border-radius:7px;box-shadow:20px 20px 50px rgba(0, 0, 0, 0.5);background-color:rgba(192, 192, 192, 0.2);\">"+ user.getId() +"</h2>\n" +
                "                <br><br><br>\n" +
                "                <div style=\"background-color:rgba(192, 192, 192, 0.2);padding:10px;text-align:left;font-size:14px;\">\n" +
                "                    <p style=\"margin:0;\">- 본 이메일은 발신전용 이메일입니다</p>\n" +
                "                </div><br>\n" +
                "                <footer style=\"padding:15px 0;bottom:0;width:100%;font-size:15px;text-align:center;font-weight:bold;\">\n" +
                "                    <p style=\"margin:0;\">부산 소프트웨어 마이스터고 학교 지원 서비스</p>\n" +
                "                    <p style=\"margin:0;\">Copyright 2022. BSM TEAM all rights reserved.</p>\n" +
                "                </footer>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "    </html>";

        MailDto mailDto = MailDto.builder()
                .to(student.getEmail())
                .subject("BSM ID 복구 메일입니다")
                .content(content)
                .build();
        mailService.sendMail(mailDto);
    }

    public void sendResetPwMail(SendResetPwMailDto dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow(
                () -> {throw new NotFoundException("없는 유저입니다, 먼저 회원가입을 해주세요");}
        );

        Date expireIn = new Date();
        expireIn.setTime(expireIn.getTime() + (5 * 60 * 1000));

        ResetPwToken token = ResetPwToken.builder()
                .token(getRandomStr(32))
                .usercode(user.getUsercode())
                .isAvailable(true)
                .expireIn(expireIn)
                .build();

        String content = "<!DOCTYPE HTML>\n" +
                "    <html lang=\"kr\">\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div style=\"display:flex;justify-content:center;\">\n" +
                "            <div style=\"padding:25px 0;text-align:center;margin:0 auto;border:solid 5px;border-radius:25px;font-family:-apple-system,BlinkMacSystemFont,'Malgun Gothic','맑은고딕',helvetica,'Apple SD Gothic Neo',sans-serif;background-color:#202124; color:#e8eaed;\">\n" +
                "                <img src=\"https://bssm.kro.kr/icons/logo.png\" alt=\"로고\" style=\"height:35px; padding-top:12px;\">\n" +
                "                <h1 style=\"font-size:28px;margin-left:25px;margin-right:25px;\">BSM 비밀번호 재설정 링크입니다</h1>\n" +
                "                <a href=\"https://bssm.kro.kr/pwReset?token=" + token + "\" style=\"display:inline-block;font-size:20px;text-decoration:none;font-weight:bold;text-align:center;margin:0;color:#e8eaed;padding:15px;border-radius:7px;box-shadow:20px 20px 50px rgba(0, 0, 0, 0.5);background-color:rgba(192, 192, 192, 0.2);\">비밀번호 재설정</a>\n" +
                "                <br><br><br>\n" +
                "                <div style=\"background-color:rgba(192, 192, 192, 0.2);padding:10px;text-align:left;font-size:14px;\">\n" +
                "                    <p style=\"margin:0;\">- 본 이메일은 발신전용 이메일입니다</p>\n" +
                "                    <p style=\"margin:0;\">- 해당 링크는 발송시점으로 부터 5분동안 유효합니다</p>\n" +
                "                </div><br>\n" +
                "                <footer style=\"padding:15px 0;bottom:0;width:100%;font-size:15px;text-align:center;font-weight:bold;\">\n" +
                "                    <p style=\"margin:0;\">부산 소프트웨어 마이스터고 학교 지원 서비스</p>\n" +
                "                    <p style=\"margin:0;\">Copyright 2022. BSM TEAM all rights reserved.</p>\n" +
                "                </footer>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "    </html>";

        MailDto mailDto = MailDto.builder()
                .to(user.getStudent().getEmail())
                .subject("BSM 비밀번호 재설정 링크입니다")
                .content(content)
                .build();
        mailService.sendMail(mailDto);
        resetPwTokenRepository.save(token);
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
