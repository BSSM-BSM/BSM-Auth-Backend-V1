package bssm.bsmauth.domain.auth.service;

import bssm.bsmauth.domain.auth.domain.TeacherAuthCode;
import bssm.bsmauth.domain.auth.domain.UserToken;
import bssm.bsmauth.domain.auth.domain.UserTokenType;
import bssm.bsmauth.domain.auth.domain.repository.TeacherAuthCodeRepository;
import bssm.bsmauth.domain.auth.domain.repository.TokenRepository;
import bssm.bsmauth.domain.auth.presentation.dto.req.*;
import bssm.bsmauth.domain.auth.presentation.dto.req.teacher.TeacherAuthCodeMailReq;
import bssm.bsmauth.domain.auth.presentation.dto.req.teacher.TeacherFindIdMailReq;
import bssm.bsmauth.domain.user.domain.Student;
import bssm.bsmauth.domain.user.domain.User;
import bssm.bsmauth.domain.user.domain.type.UserRole;
import bssm.bsmauth.domain.user.domain.repository.StudentRepository;
import bssm.bsmauth.domain.user.domain.repository.TeacherRepository;
import bssm.bsmauth.domain.user.domain.repository.UserRepository;
import bssm.bsmauth.domain.user.exception.NoSuchStudentException;
import bssm.bsmauth.domain.user.exception.NoSuchUserException;
import bssm.bsmauth.domain.user.presentation.dto.req.student.FindStudentReq;
import bssm.bsmauth.global.error.exceptions.BadRequestException;
import bssm.bsmauth.global.error.exceptions.ConflictException;
import bssm.bsmauth.global.utils.SecurityUtil;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthMailService {

    private final AuthMailProvider userMailService;

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TokenRepository tokenRepository;
    private final TeacherAuthCodeRepository teacherAuthCodeRepository;

    public void studentAuthCodeMail(FindStudentReq req) {
        Student student = studentRepository.findByGradeAndClassNoAndStudentNoAndName(
                req.getGrade(),
                req.getClassNo(),
                req.getStudentNo(),
                req.getName()
        ).orElseThrow(NoSuchStudentException::new);

        userMailService.sendAuthCodeMail(student.getEmail(), student.getAuthCode());
    }

    @Transactional
    public void teacherAuthCodeMail(TeacherAuthCodeMailReq req) {
        if (!Pattern.matches("teacher\\d.*@bssm\\.hs\\.kr", req.getEmail())) {
            throw new BadRequestException(ImmutableMap.<String, String>builder().
                    put("email", "올바른 선생님 이메일 주소가 아닙니다").
                    build()
            );
        }
        if (teacherRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new ConflictException("해당하는 이메일의 계정이 이미 존재합니다");
        }
        teacherAuthCodeRepository.deleteByEmail(req.getEmail());

        Date expireIn = new Date();
        expireIn.setTime(expireIn.getTime() + (5 * 60 * 1000));

        TeacherAuthCode authCode = TeacherAuthCode.builder()
                .token(SecurityUtil.getRandomString(8))
                .email(req.getEmail())
                .used(false)
                .type(UserTokenType.AUTH_CODE)
                .expireIn(expireIn)
                .build();
        teacherAuthCodeRepository.save(authCode);

        userMailService.sendAuthCodeMail(req.getEmail(), authCode.getToken());
    }

    public void studentFindIdMail(FindStudentReq req) {
        Student student = studentRepository.findByGradeAndClassNoAndStudentNoAndName(
                req.getGrade(),
                req.getClassNo(),
                req.getStudentNo(),
                req.getName()
        ).orElseThrow(NoSuchStudentException::new);

        User user = userRepository.findByStudent(student)
                .orElseThrow(NoSuchUserException::new);
        userMailService.sendFindAuthIdMail(student.getEmail(), user.getAuthId());
    }

    public void teacherFindIdMail(TeacherFindIdMailReq req) {
        User user = userRepository.findByRoleAndTeacherEmail(UserRole.TEACHER, req.getEmail())
                .orElseThrow(NoSuchUserException::new);
        userMailService.sendFindAuthIdMail(user.findEmailOrNull(), user.getAuthId());
    }

    @Transactional
    public void resetPwMail(ResetPwMailReq req) {
        User user = userRepository.findByAuthId(req.getAuthId())
                .orElseThrow(NoSuchUserException::new);
        String email = user.findEmailOrNull();

        UserToken token = UserToken.ofResetPw(user);
        userMailService.sendResetPwMail(email, token.getToken());
        tokenRepository.save(token);
    }
}
