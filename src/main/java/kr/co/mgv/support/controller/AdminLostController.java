package kr.co.mgv.support.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.support.dto.LostList;
import kr.co.mgv.support.service.LostService;
import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.support.vo.LostFile;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/support/lost")
public class AdminLostController {
	
	private final LostService lostService;

	@GetMapping
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
		
        return "view/admin/support/lost/list";
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
	
	@GetMapping("/detail")
	public String getLostByNo(@RequestParam("no") int lostNo, Model model) {
		Lost lost = lostService.getLostByNo(lostNo);
		List<LostFile> lostFiles = lostService.getLostFilesByLostNo(lostNo);
		model.addAttribute("lost", lost);
		model.addAttribute("lostFiles", lostFiles);
		
		return "view/admin/support/lost/detail";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("no") int lostNo, Model model) {
		lostService.deleteLost(lostNo);
		return "redirect:/admin/support/lost";
	}
	
	
	
	
}
