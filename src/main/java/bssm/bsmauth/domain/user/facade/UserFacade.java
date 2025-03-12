package bssm.bsmauth.domain.user.facade;

import bssm.bsmauth.domain.auth.exception.NoSuchTokenException;
import bssm.bsmauth.domain.user.domain.NicknameHistory;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.UserCache;
import bssm.bsmauth.domain.user.domain.repository.NicknameHistoryRepository;
import bssm.bsmauth.domain.user.domain.repository.RedisUserRepository;
import bssm.bsmauth.domain.auth.domain.repository.RefreshTokenRepository;
import bssm.bsmauth.domain.user.domain.repository.UserRepository;
import bssm.bsmauth.domain.user.exception.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;
    private final RedisUserRepository userRedisRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final NicknameHistoryRepository nicknameHistoryRepository;

    public User findByAuthIdOrNull(String authId) {
        return userRepository.findByAuthId(authId)
                .orElse(null);
    }

    public User findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByTokenAndIsAvailable(refreshToken, true)
                .orElseThrow(NoSuchTokenException::new)
                .getUser();
    }

    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);
    }

    public User findCachedUserById(long userId) {
        return userRedisRepository.findById(userId)
                .orElseGet(() -> findAndSaveUserCache(userId))
                .toUser();
    }

    public void saveUserCache(User user) {
        userRedisRepository.save(user.toUserCache());
    }

    private UserCache findAndSaveUserCache(long userId) {
        User user = findById(userId);
        saveUserCache(user);
        return user.toUserCache();
    }

    public void recordNicknameUpdate(User user, String newNickname) {
        NicknameHistory nicknameHistory = NicknameHistory.create(user, newNickname);
        nicknameHistoryRepository.save(nicknameHistory);
    }

}
