package kr.co.mgv.user.controller;

import kr.co.mgv.user.form.UserJoinForm;
import kr.co.mgv.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/form")
    public String certification(Model model) {
        UserJoinForm form = new UserJoinForm();
        model.addAttribute("userJoinForm", form);

        return "/view/auth/form";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("userJoinForm") UserJoinForm form, SessionStatus sessionStatus) {
        sessionStatus.setComplete();

        return "redirect:registered";
    }

    @ResponseBody
    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(@RequestParam String id) {
        return ResponseEntity.ok(authenticationService.loadUserByUsername(id) != null);
    }
}
