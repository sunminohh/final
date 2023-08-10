package kr.co.mgv.support.one;


import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.support.SupportCategory;
import kr.co.mgv.support.SupportTheater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support/one")
@RequiredArgsConstructor
public class OneController {

	private final OneService oneService;
	
	@GetMapping("/add")
    public String one() {
        return "/view/support/one/form";
    }
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/add")
	public String createOne(@AuthenticationPrincipal User user, AddOneForm form) {
		oneService.createOne(form);
		
		return "redirect:/support/one/add";
	}
	
	@GetMapping("/getCategory")
	@ResponseBody
	public List<SupportCategory> getCategories(@RequestParam String type) {
		
		return oneService.getgetCategoriesByType(type);
	}
	
	
	@GetMapping("/theaterByLocationNo")
	@ResponseBody
	public List<SupportTheater> getTheaterByLocationNo(@RequestParam("locationNo") int locationNo) {
		List<SupportTheater> theaterList = oneService.getTheaterByLocationNo(locationNo);
		return theaterList;
	}
	
	
	
	
}
