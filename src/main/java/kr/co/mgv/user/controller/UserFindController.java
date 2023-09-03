package kr.co.mgv.user.controller;

import kr.co.mgv.user.form.UserFindForm;
import kr.co.mgv.user.service.UserService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/pwd-form")
    public String pwdfind(UserFindForm form, Model model) {


        return "redirect:/user/auth/pass-find";
    }

    @GetMapping("pass-find")
    public String changePwdForm(UserFindForm form, Model model) {

        return "view/user/find/pass-find";
    }

    @PostMapping("/pass-find")
    public String sucPwd() {

        return "redirect:/";
    }
}
