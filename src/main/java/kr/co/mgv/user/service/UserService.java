package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.UserDao;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;



/*

    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userDao.getUserById(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            String encryptedPassword = passwordEncoder.encode(newPassword);
            userDao.updateUserById(username, encryptedPassword);
        } else {
            throw new IllegalArgumentException("Current password is incorrect");
        }
    }
*/

}
