package kr.co.mgv.support.controller;


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

import kr.co.mgv.support.dto.OneList;
import kr.co.mgv.support.form.AddOneForm;
import kr.co.mgv.support.service.LostService;
import kr.co.mgv.support.service.OneService;
import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.support.vo.One;
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
	private final LostService lostService;
	
	@GetMapping()
    public String one() {
        return "/view/support/one/form";
    }
	
	@RequestMapping("/myinquery")
	public String myinquery() {

		return "/view/support/one/list";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public OneList list(@AuthenticationPrincipal User user,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword,
			Model model) {
		
		Map<String, Object> param = new HashMap<>();
		
		param.put("userId", user.getId());
		param.put("page", page);
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
	
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		OneList oneList = oneService.search(param);
		
		return oneList;
	}
	
	
	
	@PostMapping("/add")
	public String insertOne(@AuthenticationPrincipal User user, AddOneForm form) {
		oneService.insertOne(form, user);
		
		return "redirect:/support/one";
	}
	
	@GetMapping("/getCategory")
	@ResponseBody
	public List<SupportCategory> getCategories(@RequestParam String type) {
		
		return oneService.getCategoriesByType(type);
	}
	
	@RequestMapping("/myinquery/detail")
	public String getOneByNo(@RequestParam("no") int oneNo, Model model) {
		One one = oneService.getOneByNo(oneNo);
		model.addAttribute("one", one);
		
		return "/view/support/one/detail";
	}
	
	@RequestMapping("/mylost/detail")
	public String getMyLostByNo(@RequestParam("no") int lostNo, Model model) {
		Lost lost = lostService.getLostByNo(lostNo);
		model.addAttribute("lost", lost);
		
		return "/view/support/one/lostdetail";
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
