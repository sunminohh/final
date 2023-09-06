package kr.co.mgv.event.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.event.form.AddEventForm;
import kr.co.mgv.event.service.EventService;
import kr.co.mgv.event.vo.EventCategory;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/admin/event")
@RequiredArgsConstructor
public class AdminEventController {
	
	private final EventService eventService;

    @GetMapping("/list")
    public String home() {
        return "view/admin/event/list";
    }

    @GetMapping("/insertform")
    public String eventInsertForm() {
    	return "view/admin/event/insertform";
    }
    
    @PostMapping("/add")
    public String insertEvent(@AuthenticationPrincipal User user, AddEventForm form) {
    	eventService.insertEvent(form, user);
    	return "redirect:/admin/event/list";
    }
    
    @GetMapping("/getCategory")
    @ResponseBody
    public List<EventCategory> getCategories() {
    	return eventService.getCategories();
    }
    
    
    
}