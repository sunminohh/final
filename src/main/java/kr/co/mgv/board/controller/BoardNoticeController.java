package kr.co.mgv.board.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.board.list.BoardNoticeList;
import kr.co.mgv.board.service.BoardNoticeService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
@Slf4j
public class BoardNoticeController {

	private final BoardNoticeService boardNoticeService;
	
	@GetMapping("/getNotices")
	@ResponseBody
	public ResponseEntity<BoardNoticeList> getNotices (@AuthenticationPrincipal User user){
		
		BoardNoticeList result = boardNoticeService.getNoticeByToId(user.getId());
		
		return ResponseEntity.ok().body(result);
	} 

}
