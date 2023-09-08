package kr.co.mgv.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.mgv.event.dto.EventList;
import kr.co.mgv.event.service.EventService;

@Slf4j
@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

	private final EventService eventService;
	
    @GetMapping({"/", ""})
    public String home(Model model) {
    	
    	Map<String, Object> param = new HashMap<>();
    	param.put("end", 4);
    	param.put("page", 1);
    	
    	param.put("status", "run");
    	param.put("catNo", 2);
    	EventList mgvEventList = eventService.search(param);
    	model.addAttribute("mgvResult", mgvEventList);
    	
    	param.put("catNo", 3);
    	EventList theatorEventList = eventService.search(param);
    	model.addAttribute("theatorResult", theatorEventList);
    	
    	param.put("catNo", 4);
    	EventList movieEventList = eventService.search(param);
    	model.addAttribute("movieResult", movieEventList);
    	
        return "view/event/home";
    }

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") String catNo) {
        log.info("[Event] - list: {}", catNo);
        return "view/event/list";
    }

//    @GetMapping("/end")
//    public String endList() {
//        return "view/event/end-list";
//    }
//
//    @GetMapping("/winner/list")
//    public String winnerList() {
//        return "view/event/winner-list";
//    }
//
//    @GetMapping("/winner/detail")
//    public String winnerList(@RequestParam String eventNo) {
//        return "view/event/winner-detail";
//    }

    @GetMapping("/detail/{eventNo}")
    public String detail(@PathVariable String eventNo) {
        log.info("[Event] - Detail: {}", eventNo);
        return "view/event/detail";
    }

}
