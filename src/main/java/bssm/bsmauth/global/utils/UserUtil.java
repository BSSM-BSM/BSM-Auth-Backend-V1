package bssm.bsmauth.global.utils;

import bssm.bsmauth.domain.user.domain.repositories.UserRepository;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.global.exception.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepository userRepository;

    public User getUser() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

}
