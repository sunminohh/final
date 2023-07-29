package kr.co.mgv.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @RequestMapping({"/", ""})
    public String home() {
        return "/view/movie/home";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String movieNo) {
        return "/view/movie/detail";
    }

    @GetMapping("/post")
    public String moviePost() {
        return "/view/movie/post";
    }

}
