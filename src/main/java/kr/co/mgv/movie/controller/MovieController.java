package kr.co.mgv.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @RequestMapping({"/", ""})
    public String home() {
        return "/view/movie/home";
    }

    @GetMapping("/post")
    public String moviePost() {
        return "/view/movie/post";
    }

}
