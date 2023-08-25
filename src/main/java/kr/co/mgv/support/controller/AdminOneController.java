package kr.co.mgv.support.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.support.dto.OneList;
import kr.co.mgv.support.service.OneService;
import kr.co.mgv.support.vo.One;
import kr.co.mgv.support.vo.OneFile;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/support/one")
public class AdminOneController {

	private final OneService oneService;
	
	@RequestMapping
	public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "categoryNo", required = false, defaultValue = "24") int categoryNo,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword,
			Model model) {
		
		Map<String, Object> param = new HashMap<>();
		
		param.put("categoryNo", categoryNo);
		param.put("page", page);
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
	
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		OneList oneList = oneService.search(param);
		OneList pagination = oneService.search(param);
		model.addAttribute("result", oneList);
		model.addAttribute("result", pagination);
		
		return "/view/admin/support/one/list";
	}
	
	@RequestMapping("/detail")
	public String getOneByNo(@RequestParam("no") int oneNo, Model model) {
		One one = oneService.getOneByNo(oneNo);
		List<OneFile> oneFiles = oneService.getOneFileByOneNo(oneNo);
		model.addAttribute("one", one);
		model.addAttribute("oneFiles", oneFiles);
		
		return "/view/admin/support/one/detail";
	}
	
}
