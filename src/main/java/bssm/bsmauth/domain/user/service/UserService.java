package bssm.bsmauth.domain.user.service;

import bssm.bsmauth.domain.user.domain.*;
import bssm.bsmauth.domain.user.domain.repository.*;
import bssm.bsmauth.domain.user.exception.NoSuchUserException;
import bssm.bsmauth.domain.user.facade.UserFacade;
import bssm.bsmauth.domain.user.presentation.dto.req.*;
import bssm.bsmauth.domain.user.presentation.dto.res.OtherUserRes;
import bssm.bsmauth.domain.user.presentation.dto.res.UserRes;
import bssm.bsmauth.global.auth.CurrentUser;
import bssm.bsmauth.global.error.exceptions.ConflictException;
import bssm.bsmauth.global.error.exceptions.InternalServerException;
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
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final CurrentUser currentUser;
    private final UserFacade userFacade;
    private final UserRepository userRepository;

    @Value("${env.file.path.base}")
    private String REAL_RESOURCE_PATH;
    @Value("${env.file.path.publicBase}")
    private String PUBLIC_RESOURCE_PATH;
    @Value("${env.file.path.upload.profile}")
    private String PROFILE_UPLOAD_RESOURCE_PATH;

    public UserRes findMyInfo() {
        return currentUser.findCachedUser()
                .toUserResponse();
    }

    public OtherUserRes getOtherUserInfo(long userCode) {
        return userFacade.findCachedUserByCode(userCode)
                .toOtherUserResponse();
    }

    @Transactional
    public void updateNickname(UpdateNicknameReq dto) {
        userRepository.findByNickname(dto.getNewNickname())
                .ifPresent(u -> {throw new ConflictException("이미 존재하는 닉네임 입니다");});
        User user = currentUser.findUser();
        user.updateNickname(dto.getNewNickname());
        userFacade.saveUserCache(user);
    }

    @Transactional
    public void uploadProfile(MultipartFile file) {
        User user = currentUser.findUser();
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
        String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String fileId = String.valueOf(user.getCode());

        File dir = new File(REAL_RESOURCE_PATH + PROFILE_UPLOAD_RESOURCE_PATH);
        File newFile = new File(dir.getPath() + "/" + fileId + "." + fileExt);

        if (!dir.exists()) dir.mkdirs();
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("프로필 사진 업로드에 실패하였습니다");
        }

        String newFileName = fileId + ".png";
        int newImageSize = 128;
        try {
            BufferedImage image = ImageIO.read(newFile);
            BufferedImage newImage = new BufferedImage(newImageSize, newImageSize, BufferedImage.TYPE_INT_RGB);

            Image resizedImage = image.getScaledInstance(newImageSize, newImageSize, Image.SCALE_REPLICATE);
            newImage.createGraphics().drawImage(resizedImage, 0, 0, Color.white, null);

            newFile.renameTo(new File(dir.getPath() + "/" + newFileName));
            ImageIO.write(newImage, "png", newFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerException("프로필 사진 변환에 실패하였습니다");
        }
        user.updateProfileUrl(PUBLIC_RESOURCE_PATH + PROFILE_UPLOAD_RESOURCE_PATH + newFileName);
    }
}
