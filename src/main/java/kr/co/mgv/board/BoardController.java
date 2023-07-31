package kr.co.mgv.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/theater/list")
    public String theaterList() {
        return "/view/board/list";
    }

    @GetMapping("/theater/detail")
    public String theaterDetail() {
        return "/view/board/detail";
    }

}
