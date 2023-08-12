package kr.co.mgv.support.controller;


import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.support.form.AddOneForm;
import kr.co.mgv.support.service.OneService;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support/one")
@RequiredArgsConstructor
public class OneController {

	private final OneService oneService;
	
	@GetMapping()
    public String one() {
        return "/view/support/one/form";
    }
	
	@GetMapping("/myinquery")
	public String myinquery() {
		return "/view/support/one/list";
	}
	
	@PostMapping("/add")
	public String createOne(@AuthenticationPrincipal User user, AddOneForm form) {
		oneService.createOne(form, user);
		
		return "redirect:/support/one";
	}
	
	@GetMapping("/getCategory")
	@ResponseBody
	public List<SupportCategory> getCategories(@RequestParam String type) {
		
		return oneService.getCategoriesByType(type);
	}
	
	@GetMapping("/getLocation")
	@ResponseBody
	public List<Location> getLocations() {
		
		return oneService.getLocations();
	}
	
	@GetMapping("/getTheaterByLocationNo")
	@ResponseBody
	public List<Theater> getTheatersByLocationNo(@RequestParam("locationNo") int locationNo) {
		
		return oneService.getTheatesrByLocationNo(locationNo);
	}
	
	
	
	
}
