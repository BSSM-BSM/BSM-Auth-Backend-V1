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
        long userId = Long.parseLong(authenticationName);
        return userFacade.findById(userId);
    }

    public User findUserOrNull() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (authenticationName.equals("anonymousUser")) return null;
        long userId = Long.parseLong(authenticationName);
        return userFacade.findById(userId);
    }

    @Transactional(readOnly = true)
    public User findCachedUser() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        long userId = Long.parseLong(authenticationName);
        return userFacade.findCachedUserById(userId);
    }

    @Transactional(readOnly = true)
    public User findCachedUserOrNull() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (authenticationName.equals("anonymousUser")) return null;
        long userId = Long.parseLong(authenticationName);
        return userFacade.findCachedUserById(userId);
    }

}
