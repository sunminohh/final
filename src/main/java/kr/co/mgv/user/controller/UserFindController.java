package kr.co.mgv.user.controller;

import kr.co.mgv.user.form.UserFindForm;
import kr.co.mgv.user.service.EmailServiceImpl;
import kr.co.mgv.user.service.UserService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserFindController {

    private final UserService userService;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;

    // todo find
    @GetMapping("/user-find")
    public String findForm() {
        return "view/user/find/user-find";
    }

    @PostMapping("/user-find")
    @ResponseBody
    public ResponseEntity<?> userfind(@RequestParam String name,
                                      @RequestParam String birth,
                                      @RequestParam String email) {
        User user = userService.getUserByEmail(email);

        if (user != null && user.getName().equals(name) && user.getBirth().toString().equals(birth)) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("회원정보가 없습니다.");
        }
    }

    @GetMapping("/pwd-form")
    public String pwdfindForm() {
        return "view/user/find/pwdform";
    }

    @PostMapping("/check")
    @ResponseBody
    public ResponseEntity<String> UserCheck(UserFindForm form) {
        User user = userService.getUserById(form.getId());

        if (user != null && user.getName().equals(form.getName()) && user.getEmail().equals(form.getEmail())) {
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.badRequest().body("회원정보가 없습니다. 다시 시도해주세요.");
        }

    }

    @PostMapping("/tempPwd")
    @ResponseBody
    public ResponseEntity<String> sendMail(@RequestParam("email") String email,
                                           @RequestParam("id") String userId) {
        log.info("사용자 입력 이메일 -> {}", email);
        User user = userService.getUserById(userId);
        log.info("사용자 입력 아이디 -> {}", userId);

        try {
            // 임시비밀번호
            String tempPwd = emailService.sendTempPwdMessage(email);
            log.info("임시 비밀번호 -> {}", tempPwd);
            userService.updatePassword(user.getId(), passwordEncoder.encode(tempPwd));

            return ResponseEntity.ok("success");

        } catch (Exception e) {
            log.error("Error sending email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메일 전송 중 오류가 발생했습니다. 잠시 후 시도해주세요.");
        }
    }

}
