package bssm.bsmauth.domain.user.domain.repository;

import bssm.bsmauth.domain.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static bssm.bsmauth.domain.user.domain.QUser.user;
import static bssm.bsmauth.domain.user.domain.QStudent.student;
import static bssm.bsmauth.domain.user.domain.QTeacher.teacher;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> findNicknameHistory(String nickname) {
        User currentNicknameUser = jpaQueryFactory.selectFrom(user)
                .leftJoin(user.student, student)
                .leftJoin(user.teacher, teacher)
                .fetchJoin()
                .distinct()
                .where(
                        user.nickname.like(nickname)
                )
                .fetchOne();

        List<User> userList = jpaQueryFactory.selectFrom(user)
                .leftJoin(user.student, student)
                .leftJoin(user.teacher, teacher)
                .fetchJoin()
                .distinct()
                .where(
                        user.nickname.notLike(nickname),
                        user.nicknameHistories.any().nickname.contains(nickname)
                )
                .orderBy(
                        user.nicknameHistories.any().modifiedAt.desc()
                )
                .fetch();
        if (currentNicknameUser != null) {
            userList.add(0, currentNicknameUser);
        }
        return userList;
    }

}
