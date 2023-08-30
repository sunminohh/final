package kr.co.mgv.board.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.mgv.board.list.MyBoardList;
import kr.co.mgv.board.service.MyBoardService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/mypage/board")
@RequiredArgsConstructor
@Slf4j
public class UserBoardController {
	
	private final MyBoardService myBoardService;

	@GetMapping("/list")
	public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
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
		
		int totalRows = myBoardService.getBoardsTotalRows(id);
		model.addAttribute("totalRows", totalRows);
		int CtotalRows = myBoardService.getCommentTotalRows(id);
		model.addAttribute("CtotalRows", CtotalRows);
		int LtotalRows = myBoardService.getlikeTotalRows(id);
		model.addAttribute("LtotalRows", LtotalRows);
		
		MyBoardList result = myBoardService.getBoardList(param);
		model.addAttribute("result", result);
		
		
		return "view/user/board/list";
	}

	@GetMapping("/commentList")
	public String commentList(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
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
		
		int totalRows = myBoardService.getBoardsTotalRows(id);
		model.addAttribute("totalRows", totalRows);
		int CtotalRows = myBoardService.getCommentTotalRows(id);
		model.addAttribute("CtotalRows", CtotalRows);
		int LtotalRows = myBoardService.getlikeTotalRows(id);
		model.addAttribute("LtotalRows", LtotalRows);
		
		MyBoardList result = myBoardService.getBoardListByComment(param);
		model.addAttribute("result", result);
		
		return "view/user/board/commentList";
	}

	@GetMapping("/likeList")
	public String likeList(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
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
		
		int totalRows = myBoardService.getBoardsTotalRows(id);
		model.addAttribute("totalRows", totalRows);
		int CtotalRows = myBoardService.getCommentTotalRows(id);
		model.addAttribute("CtotalRows", CtotalRows);
		int LtotalRows = myBoardService.getlikeTotalRows(id);
		model.addAttribute("LtotalRows", LtotalRows);
		
		MyBoardList result = myBoardService.getBoardListByLike(param);
		model.addAttribute("result", result);
		
		return "view/user/board/likeList";
	}

	@GetMapping("/joinList")
	public String joinList(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "opt", required = false, defaultValue = "") String opt,
			@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(name = "com", required = false, defaultValue = "all") String com,
			@AuthenticationPrincipal User user,
			Model model) {
		
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("page", page);
		String id = user.getId();
		param.put("id", id);
		param.put("com", com);
		
		log.info("{}",id);
		
		if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
			param.put("opt", opt);
			param.put("keyword", keyword);
		}
		
		int totalRows = myBoardService.getBoardsTotalRows(id);
		model.addAttribute("totalRows", totalRows);
		int CtotalRows = myBoardService.getCommentTotalRows(id);
		model.addAttribute("CtotalRows", CtotalRows);
		int LtotalRows = myBoardService.getlikeTotalRows(id);
		model.addAttribute("LtotalRows", LtotalRows);
		
		MyBoardList result = myBoardService.getBoardListByJoin(param);
		model.addAttribute("result", result);
		
		return "view/user/board/joinList";
	}
}
