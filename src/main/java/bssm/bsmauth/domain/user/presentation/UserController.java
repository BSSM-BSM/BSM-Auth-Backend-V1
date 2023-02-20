package bssm.bsmauth.domain.user.presentation;

import bssm.bsmauth.domain.user.presentation.dto.res.*;
import bssm.bsmauth.domain.user.service.UserService;
import bssm.bsmauth.domain.user.presentation.dto.req.*;
import bssm.bsmauth.global.auth.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CurrentUser currentUser;

    @GetMapping
    public UserRes getUserInfo() {
        return currentUser.getUser()
                .toUserResponse();
    }

    @GetMapping("{userCode}")
    public OtherUserRes getOtherUserInfo(@PathVariable long userCode) {
        return userService.getOtherUserInfo(userCode);
    }

    @PutMapping("nickname")
    public void updateNickname(@Valid @RequestBody UpdateNicknameReq dto) {
        userService.updateNickname(currentUser.getUser(), dto);
    }

    @PostMapping("profile")
    public void uploadProfile(@RequestParam MultipartFile file) {
        userService.uploadProfile(currentUser.getUser(), file);
    }
}
