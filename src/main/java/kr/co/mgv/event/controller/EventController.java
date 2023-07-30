package kr.co.mgv.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/event")
public class EventController {

    @GetMapping({"/", ""})
    public String home() {
        return "/view/event/home";
    }

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") String cateNo) {
        log.info("[Event] - list: {}", cateNo);
        return "/view/event/list";
    }

    @GetMapping("/end")
    public String endList() {
        return "/view/event/end-list";
    }

    @GetMapping("/winner/list")
    public String winnerList() {
        return "/view/event/winner-list";
    }

    @GetMapping("/winner/detail")
    public String winnerList(@RequestParam String eventNo) {
        return "/view/event/winner-detail";
    }

    @GetMapping("/detail/{eventNo}")
    public String detail(@PathVariable String eventNo) {
        log.info("[Event] - Detail: {}", eventNo);
        return "/view/event/detail";
    }

}
