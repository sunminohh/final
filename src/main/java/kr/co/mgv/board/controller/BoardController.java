package kr.co.mgv.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.board.list.BoardList;
import kr.co.mgv.board.list.MyBoardList;
import kr.co.mgv.board.service.MyBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board/user")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

	private final MyBoardService myBoardService;
	
	@GetMapping("/list")
	public String userList(@RequestParam("id") String id, 
			        @RequestParam(name = "page", required = false, defaultValue = "1") int page,
			        @RequestParam(name = "boards", required = false, defaultValue = "all") String boards,
			        @RequestParam(name = "opt", required = false, defaultValue = "") String opt,
			        @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
			        Model model) {
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("page", page);
		param.put("boards", boards);
		param.put("id", id);
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
		
		return "view/board/user/list";
	}
	
	@GetMapping("/ajaxList")
	@ResponseBody
	public ResponseEntity<MyBoardList> ajaxList(@RequestParam("id") String id, 
	        @RequestParam(name = "page", required = false, defaultValue = "1") int page,
	        @RequestParam(name = "boards", required = false, defaultValue = "all") String boards,
	        @RequestParam(name = "opt", required = false, defaultValue = "") String opt,
	        @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("page", page);
		param.put("boards", boards);
		param.put("id", id);
		if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
			param.put("opt", opt);
			param.put("keyword", keyword);
		}
		
		MyBoardList result = myBoardService.getBoardList(param);
		
		return ResponseEntity.ok().body(result);
	}

}
