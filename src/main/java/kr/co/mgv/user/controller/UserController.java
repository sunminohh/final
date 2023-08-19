package kr.co.mgv.user.controller;

import kr.co.mgv.user.service.AuthenticationService;
import kr.co.mgv.user.service.EmailServiceImpl;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@Secured("ROLE_USER")
@RequestMapping("/user/info")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationService authenticationService;
    private final EmailServiceImpl emailService;

    @RequestMapping({"/",""})
    public String home(@AuthenticationPrincipal User user, Model model) {
        String userId = user.getId();
        String userName = user.getName();
        String userEmail = user.getEmail();

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);

        return "view/user/home";
    }

    // 이메일 인증
    @GetMapping("/auth")
    public String emailAuthForm(@AuthenticationPrincipal User user, Model model) {
        String userId = user.getId();
        String userName = user.getName();
        String userEmail = user.getEmail();

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);

        return "view/user/info/auth";
    }

    @PostMapping("/auth")
    public String success() {
        return "redirect:/user/info/form";
    }

    // 이메일 인증 후 회원정보 폼
    @GetMapping("/form")
    public String myMGV(@AuthenticationPrincipal User user, Model model) {
        String id = user.getId();
        String name = user.getName();
        LocalDate birth = user.getBirth();
        String email = user.getEmail();
        // todo zipcode

        model.addAttribute("userId", id);
        model.addAttribute("userName", name);
        model.addAttribute("userBirth", birth);
        model.addAttribute("userEmail", email);

        return "view/user/info/form";
    }

    @PostMapping("/form")
    public String updateForm() {

        return "redirect:/user/info/home";
    }

    // 비밀번호 수정 -> 비밀번호 체크 & 변경 폼
    @GetMapping("/pwd-check")
    public String pwdCheck() {
        return "view/user/info/pwd-check";
    }

    @PostMapping("/pwd-check")
    @ResponseBody
    public ResponseEntity<String> updatePwd() {

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/booking")
    public String booking() {
        return "view/user/booking/list";
    }

    @GetMapping("/ticket")
    public String ticekt() {
        return "view/user/ticket/list";
    }

    @GetMapping("/moviestory")
    public String moviestory() {
        return "view/user/moviestory/list";
    }

    @GetMapping("/inquiry")
    public String inquiry() {

        return "view/user/inquiry/list";
    }


}
