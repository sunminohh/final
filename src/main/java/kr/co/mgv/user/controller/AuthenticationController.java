package kr.co.mgv.user.controller;

import kr.co.mgv.user.form.UserJoinForm;
import kr.co.mgv.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/form")
    public String createForm(Model model) {
        UserJoinForm form = new UserJoinForm();
        model.addAttribute("userJoinForm", form);

        return "/view/auth/form";
    }

    @PostMapping("/form")
    public String form(@Valid UserJoinForm form, Errors errors, Model model) {
        return "redirect:/";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("userJoinForm") UserJoinForm form, SessionStatus sessionStatus) {
        // TODO Service -> DAO (user, role)
        sessionStatus.setComplete();
        return "redirect:/user/auth/registered";
    }

    @GetMapping("/registered")
    public String registered() {
        return "/view/auth/registered";
    }

    @ResponseBody
    @GetMapping("/checkId")
    public ResponseEntity<Boolean> checkId(@RequestParam String id) {
        return ResponseEntity.ok(authenticationService.loadUserByUsername(id) != null);
    }

    @ResponseBody
    @GetMapping("/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.getUserByEmail(email) != null);
    }
}
