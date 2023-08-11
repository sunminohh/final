package kr.co.mgv.support.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.support.form.AddLostForm;
import kr.co.mgv.support.service.LostService;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support/lost")
@RequiredArgsConstructor
public class LostController {
	
	private final LostService lostService;

	@GetMapping()
    public String lost() {
        return "/view/support/lost/list";
    }
	 
	@GetMapping("/form")
    public String lostForm() {
        return "/view/support/lost/form";
    }
	
	@PostMapping("/add")
	public String createLost(@AuthenticationPrincipal User user, AddLostForm form) {
		lostService.insertLost(form, user);
		
		return "redirect:/support/lost";
	}
	
	@GetMapping("/getLocation")
	@ResponseBody
	public List<Location> getLocations() {
		
		return lostService.getLocations();
	}
	
	@GetMapping("/getTheaterByLocationNo")
	@ResponseBody
	public List<Theater> getTheatersByLocationNo(@RequestParam("locationNo") int locationNo) {
		
		return lostService.getTheatesrByLocationNo(locationNo);
	}
}















