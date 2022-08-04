package bssm.bsmauth.user;

import bssm.bsmauth.user.dto.request.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public void signUp(@RequestBody UserSignUpDto dto) throws Exception {
        userService.signUp(dto);
    }
}
