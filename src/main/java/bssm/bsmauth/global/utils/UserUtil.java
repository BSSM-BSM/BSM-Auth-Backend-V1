package bssm.bsmauth.global.utils;

import bssm.bsmauth.global.auth.UserInfo;
import bssm.bsmauth.user.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    public User getCurrentUser() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userInfo.getUser();
    }
}
