package kr.co.mgv.theater.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/theater")
public class TheaterController {

    @GetMapping({"/", ""})
    public String home() {
        return "/view/theater/home";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam(defaultValue = "1") String brchNo) {
        return "/view/theater/detail";
    }

}
