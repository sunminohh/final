package kr.co.mgv.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/support")
public class SupportController {

    @RequestMapping({"/", ""})
    public String home() {
        return "/view/support/home";
    }

}
