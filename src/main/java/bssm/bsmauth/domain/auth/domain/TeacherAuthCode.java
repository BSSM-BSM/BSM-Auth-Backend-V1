package bssm.bsmauth.domain.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class TeacherAuthCode extends UserToken {

    @Column(length = 32)
    private String email;
}
