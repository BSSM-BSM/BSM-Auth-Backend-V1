package bssm.bsmauth.domain.user.facade;

import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.repository.RedisUserRepository;
import bssm.bsmauth.domain.user.domain.repository.RefreshTokenRepository;
import bssm.bsmauth.domain.user.presentation.dto.response.UserResponse;
import bssm.bsmauth.global.error.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final RedisUserRepository userRedisRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public User getByAvailableRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByTokenAndIsAvailable(refreshToken, true)
                .orElseThrow(() -> new NotFoundException("토큰을 찾을 수 없습니다"))
                .getUser();
    }

    public User getCachedUserByCode(long userCode) {
        return userRedisRepository.findById(userCode)
                .orElseThrow(NotFoundException::new)
                .toUser();
    }

    public void saveCacheUser(User user) {
        System.out.println("user = " + user.getCode());
        userRedisRepository.save(user.toUserRedis());
    }

}
