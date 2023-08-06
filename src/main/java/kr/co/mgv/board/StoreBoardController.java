package kr.co.mgv.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board/store")
public class StoreBoardController {

    @GetMapping("/list")
    public String theaterList() {
        return "/view/board/store/list";
    }

    @GetMapping("/detail")
    public String theaterDetail() {
        return "/view/board/store/detail";
    }

    @GetMapping("/add")
    public String theaterForm() {
        return "/view/board/store/form";
    }

}