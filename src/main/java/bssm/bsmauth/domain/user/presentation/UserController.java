package bssm.bsmauth.domain.user.presentation;

import bssm.bsmauth.domain.user.presentation.dto.res.*;
import bssm.bsmauth.domain.user.service.UserService;
import bssm.bsmauth.domain.user.presentation.dto.req.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserRes getUserInfo() {
        return userService.findMyInfo();
    }

    @GetMapping("{userCode}")
    public OtherUserRes getOtherUserInfo(@PathVariable long userCode) {
        return userService.getOtherUserInfo(userCode);
    }

    @GetMapping("nickname")
    public List<UserNicknameHistoryRes> findUserByNicknameHistory(@RequestParam String nickname) {
        return userService.findUserByNicknameHistory(nickname);
    }

    @PutMapping("nickname")
    public void updateNickname(@Valid @RequestBody UpdateNicknameReq req) {
        userService.updateNickname(req);
    }

    @PostMapping("profile")
    public void uploadProfile(@RequestParam MultipartFile file) {
        userService.uploadProfile(file);
    }
}
