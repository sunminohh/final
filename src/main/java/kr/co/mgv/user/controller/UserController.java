package kr.co.mgv.user.controller;

import kr.co.mgv.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/info")
@Slf4j
public class UserController {

    @GetMapping("/home")
    @ResponseBody
    public ResponseEntity<String> home(@AuthenticationPrincipal User user) {
        if (user != null) {
            log.info("사용자 -> {}", user);
            return ResponseEntity.ok("authenticated");
        } else {
            log.info("사용자 -> {}", (Object) null);
            return ResponseEntity.ok("anonymous");
        }
    }
    // todo 기존 코드
    /*
    @RequestMapping({"/",""})
    public String home() {
        return "view/user/home";
    }
    */

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

    @GetMapping("/myinfo")
    public String myinfo() {
        return "view/user/myinfo/form";
    }

    // 나의 정보
    @GetMapping("/myinfo/pwdform")
    public String pwdform() {
        return "view/user/myinfo/pwdform";
    }
}
