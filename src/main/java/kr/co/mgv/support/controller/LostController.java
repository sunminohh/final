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

import kr.co.mgv.support.dto.LostList;
import kr.co.mgv.support.form.AddLostForm;
import kr.co.mgv.support.service.LostService;
import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Controller
@RequestMapping("/support/lost")
@RequiredArgsConstructor
@ToString
public class LostController {
	
	private final LostService lostService;

	@RequestMapping
    public String lost(	@RequestParam(name = "locationNo", required = false, defaultValue = "0") int locationNo,
			@RequestParam(name = "theaterNo", required = false, defaultValue = "0") int theaterNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword,
    		Model model) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("page", page);
		
		if (locationNo != 0) {
			param.put("locationNo", locationNo);
		}
		
		if (theaterNo != 0) {
			param.put("theaterNo", theaterNo);
		} 
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
	
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		LostList lostList = lostService.search(param);
		model.addAttribute("result", lostList);
		
        return "/view/support/lost/list";
    }
	
	
	@GetMapping("/list")
	@ResponseBody
	public LostList list(
			@RequestParam(name = "locationNo", required = false, defaultValue = "0") int locationNo,
			@RequestParam(name = "theaterNo", required = false, defaultValue = "0") int theaterNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("page", page);
		
		if (locationNo != 0) {
			param.put("locationNo", locationNo);
		}
		
		if (theaterNo != 0) {
			param.put("theaterNo", theaterNo);
		} 
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
	
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		LostList lostList = lostService.search(param);
		
		return lostList;
	}
	
	@GetMapping("/mylist")
	@ResponseBody
	public LostList list(@AuthenticationPrincipal User user,
			@RequestParam(name = "locationNo", required = false, defaultValue = "0") int locationNo,
			@RequestParam(name = "theaterNo", required = false, defaultValue = "0") int theaterNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword) {
		
		Map<String, Object> param = new HashMap<>();
		
		param.put("userId", user.getId());
		param.put("page", page);
		
		if (locationNo != 0) {
			param.put("locationNo", locationNo);
		}
		
		if (theaterNo != 0) {
			param.put("theaterNo", theaterNo);
		} 
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
		
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		LostList lostList = lostService.search(param);
		
		return lostList;
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
	
	@RequestMapping("/detail")
	public String getLostByNo(@RequestParam("no") int lostNo, Model model) {
		Lost lost = lostService.getLostByNo(lostNo);
		model.addAttribute("lost", lost);
		
		return "/view/support/lost/detail";
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















