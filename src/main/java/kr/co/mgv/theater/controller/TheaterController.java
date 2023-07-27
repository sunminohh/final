package kr.co.mgv.theater.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/theater")
public class TheaterController {

    @RequestMapping({"/", ""})
    public String home() {
        return "/view/theater/home";
    }

}
