package kr.co.mgv.user.controller;

import kr.co.mgv.user.form.UserFindForm;
import kr.co.mgv.user.service.UserService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    // todo find
    @GetMapping("/user-find")
    public String findForm() {
        return "view/user/find/user-find";
    }

    @PostMapping("/user-find")
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
    public ResponseEntity<String> UserCheck(UserFindForm form) {
        User user = userService.getUserById(form.getId());

        if (user != null && user.getName().equals(form.getName()) && user.getEmail().equals(form.getEmail())) {
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.badRequest().body("회원정보가 없습니다. 다시 시도해주세요.");
        }

    }

    @PostMapping("pwd-find")
    public ResponseEntity<String> pwdfind(UserFindForm form) {
        User user = userService.getUserById(form.getId());

        return ResponseEntity.ok("ok");

    }
}
