package kr.co.mgv.support.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support")
@RequiredArgsConstructor
public class HomeCotroller {
	

    @RequestMapping({"/", ""})
    public String home() {
        return "view/support/home";
    }
    

    @GetMapping("/terms")
    public String terms() {
        return "view/support/terms/list";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "view/support/privacy-policy/list";
    }
}
