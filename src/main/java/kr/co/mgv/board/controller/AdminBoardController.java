package kr.co.mgv.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.mgv.board.list.MyBoardList;
import kr.co.mgv.board.service.AdminBoardService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/board")
@RequiredArgsConstructor
@Slf4j
public class AdminBoardController {

	private final AdminBoardService adminBoardService;
	
	@GetMapping("/reportList")
	public String adminReportList(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
								   @RequestParam(name = "opt", required = false, defaultValue = "") String opt,
						           @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
						           @RequestParam(name = "boards", required = false, defaultValue = "all") String boards,
						           @AuthenticationPrincipal User user,
						           Model model) {
			
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("page", page);
		String id = user.getId();
		param.put("id", id);
		param.put("boards", boards);
		
		log.info("{}",id);
		
		if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
			param.put("opt", opt);
			param.put("keyword", keyword);
		}
		
		MyBoardList result = adminBoardService.getBoardList(param);
		model.addAttribute("result", result);
		
		return "view/admin/board/reportList";
	}
}
