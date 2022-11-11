package bssm.bsmauth.global.utils;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserFacade userFacade;

    public User getUser() {
        String userCode = SecurityContextHolder.getContext().getAuthentication().getName();
        return userFacade.getCachedUserByCode(Long.parseLong(userCode));
    }

}
