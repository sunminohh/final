package kr.co.mgv.user.service;

import kr.co.mgv.user.mapper.AuthenticationDao;
import kr.co.mgv.user.mapper.UserRoleDao;
import kr.co.mgv.user.vo.User;
import kr.co.mgv.user.vo.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationService implements UserDetailsService {

    private final AuthenticationDao authenticationDao;
    private final JavaMailSender javaMailSender;
    private final UserRoleDao userRoleDao;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createUser(User user) {

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // 테이블에 사용자정보 저장
        authenticationDao.insertUser(user);

        // 사용자 보유권한 저장
        UserRole role = new UserRole();
        role.setUser(user);
        role.setRoleName("ROLE_USER");

        userRoleDao.insertUserRole(role);
    }

    // 로그인
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = authenticationDao.getUserById(id);
        if (user != null) {
            user.setRoleName(
                userRoleDao.getUserRoleByUserId(id).stream()
                        .map(UserRole::getRoleName)
                        .collect(Collectors.toList())
            );
        }
        return user;
    }

    public User getUserByEmail(String email) {
        return authenticationDao.getUserByEmail(email);
    }

}
