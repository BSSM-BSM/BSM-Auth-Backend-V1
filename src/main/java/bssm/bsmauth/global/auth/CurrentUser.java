package bssm.bsmauth.global.auth;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUser {

    private final UserFacade userFacade;

    public User getUser() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        long userCode = Long.parseLong(authenticationName);
        return userFacade.findCachedUserByCode(userCode);
    }

    public User getUserOrNull() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (authenticationName.equals("anonymousUser")) return null;
        long userCode = Long.parseLong(authenticationName);
        return userFacade.findCachedUserByCode(userCode);
    }

}
