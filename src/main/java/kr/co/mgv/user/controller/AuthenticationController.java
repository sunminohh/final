package kr.co.mgv.user.controller;

import kr.co.mgv.user.form.UserJoinForm;
import kr.co.mgv.user.service.AuthenticationService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    // 회원가입
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
    public String join(@ModelAttribute("userJoinForm") UserJoinForm form, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
        // TODO Service -> DAO (user, role)
        User user = new User();
        user.setId(form.getId());
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setBirth(form.getBirth());
        user.setPassword(form.getPassword());

        authenticationService.createUser(user);

        UserDetails userDetails = authenticationService.loadUserByUsername(form.getId());
        redirectAttributes.addFlashAttribute("user", userDetails);

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

    // 로그인
    @ResponseBody
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(HttpSession session, @RequestBody User user) { // todo
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            User userPrincipal = (User) auth.getPrincipal(); // todo
            log.info("User [{}] logged in.", userPrincipal.getUsername());
            return ResponseEntity.ok(userPrincipal); // todo
        } catch (BadCredentialsException e) {
            log.warn("User [{}] failed login: {}", user.getUsername(), e.getLocalizedMessage());
            return ResponseEntity
                .badRequest()
                .body(e.getLocalizedMessage()); // todo
        }
    }

}
