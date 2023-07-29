package kr.co.mgv.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/event")
public class EventController {

    @GetMapping({"/", ""})
    public String home() {
        return "/view/event/home";
    }

    @GetMapping("/list/{cateNo}")
    public String list(@PathVariable String cateNo) {
        log.info("[Event] - list: {}", cateNo);
        return "/view/event/list";
    }

    @GetMapping("/detail/{eventNo}")
    public String detail(@PathVariable String eventNo) {
        log.info("[Event] - Detail: {}", eventNo);
        return "/view/event/detail";
    }

}
