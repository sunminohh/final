package kr.co.mgv.benefit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/benefit")
public class BenefitController {

    @GetMapping({"", "/"})
    public String membershipHome() {
        return "view/benefit/membership";
    }

    @GetMapping("/discount")
    public String discountHome() {
        return "view/benefit/discount";
    }

}
