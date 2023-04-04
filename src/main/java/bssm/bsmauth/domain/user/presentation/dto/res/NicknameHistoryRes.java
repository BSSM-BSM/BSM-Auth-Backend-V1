package bssm.bsmauth.domain.user.presentation.dto.res;

import bssm.bsmauth.domain.user.domain.NicknameHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NicknameHistoryRes {

    private String nickname;
    private LocalDateTime modifiedAt;


    public static NicknameHistoryRes create(NicknameHistory nicknameHistory) {
        NicknameHistoryRes res = new NicknameHistoryRes();
        res.nickname = nicknameHistory.getNickname();
        res.modifiedAt = nicknameHistory.getModifiedAt();
        return res;
    }
}
