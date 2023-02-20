package bssm.bsmauth.global.auth;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CurrentUser {

    private final UserFacade userFacade;

    public User findUser() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        long userCode = Long.parseLong(authenticationName);
        return userFacade.findByCode(userCode);
    }

    public User findUserOrNull() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (authenticationName.equals("anonymousUser")) return null;
        long userCode = Long.parseLong(authenticationName);
        return userFacade.findByCode(userCode);
    }

    @Transactional(readOnly = true)
    public User findCachedUser() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        long userCode = Long.parseLong(authenticationName);
        return userFacade.findCachedUserByCode(userCode);
    }

    @Transactional(readOnly = true)
    public User findCachedUserOrNull() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (authenticationName.equals("anonymousUser")) return null;
        long userCode = Long.parseLong(authenticationName);
        return userFacade.findCachedUserByCode(userCode);
    }

}
