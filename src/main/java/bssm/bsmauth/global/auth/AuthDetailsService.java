package bssm.bsmauth.global.auth;

import bssm.bsmauth.domain.user.facade.UserFacade;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import bssm.bsmauth.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final UserFacade userFacade;

    @Override
    public UserDetails loadUserByUsername(String authenticationName) throws UsernameNotFoundException {
        long userId = Long.parseLong(authenticationName);
        return new AuthDetails(userFacade.findCachedUserById(userId));
    }

}
