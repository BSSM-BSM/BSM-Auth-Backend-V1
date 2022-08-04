package bssm.bsmauth.user;

import bssm.bsmauth.global.exceptions.BadRequestException;
import bssm.bsmauth.global.exceptions.ConflictException;
import bssm.bsmauth.global.exceptions.InternalServerException;
import bssm.bsmauth.global.exceptions.NotFoundException;
import bssm.bsmauth.user.dto.request.UserLoginDto;
import bssm.bsmauth.user.dto.request.UserSignUpDto;
import bssm.bsmauth.user.dto.request.UserUpdateNicknameDto;
import bssm.bsmauth.user.dto.request.UserUpdatePwDto;
import bssm.bsmauth.user.entities.Student;
import bssm.bsmauth.user.entities.User;
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
    @Value("${PUBLIC_RESOURCE_PATH}")
    private String PUBLIC_RESOURCE_PATH;
    @Value("${PROFILE_UPLOAD_RESOURCE_PATH}")
    private String PROFILE_UPLOAD_RESOURCE_PATH;

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
        String salt = createSalt();
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
        String newSalt = createSalt();
        // 새 비밀번호 암호화
        String newPw = encryptPw(newSalt, dto.getNewPw());

        newUser.setPw(newPw);
        newUser.setPwSalt(newSalt);
        userRepository.save(newUser);
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

    private String createSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return HexFormat.of().formatHex(randomBytes);
    }

    private String encryptPw(String salt, String pw) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA3-256");
        messageDigest.update((salt + pw).getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(messageDigest.digest());
    }
}
