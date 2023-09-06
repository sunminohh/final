package kr.co.mgv.event.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.event.dto.EventList;
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

	/*
	 * @GetMapping() public String event(@RequestParam(name = "catNo", required =
	 * false, defaultValue= "1") int catNo,
	 * 
	 * @RequestParam(name = "page", required = false, defaultValue = "1") int page,
	 * 
	 * @RequestParam(name = "keyword", required = false) String keyword, Model
	 * model) {
	 * 
	 * Map<String, Object> param = new HashMap<>(); param.put("catNo", catNo);
	 * param.put("page", page); EventList eventList = eventService.search(param);
	 * 
	 * model.addAttribute("result", eventList);
	 * 
	 * return "view/admin/event/list"; }
	 */
    
    @GetMapping("/list")
    public String getEvent(@RequestParam(name = "catNo", required = false, defaultValue= "1") int catNo,
    		@RequestParam(name = "status", required = false, defaultValue = "") String status,
    		@RequestParam(name = "page", required = false, defaultValue = "1") int page,
    		@RequestParam(name = "keyword", required = false) String keyword,
    		Model model) {
    	
    	Map<String, Object> param = new HashMap<>();
    	param.put("catNo", catNo);
    	param.put("page", page);
    	
    	if (StringUtils.hasText(status)) {
    		param.put("status", status);
    	}
    	
    	if (StringUtils.hasText(keyword)) {
    		param.put("keyword", keyword);
    	}
    	
    	EventList eventList = eventService.search(param);
    	model.addAttribute("result", eventList);
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