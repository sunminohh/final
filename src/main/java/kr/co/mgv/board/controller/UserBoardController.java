package kr.co.mgv.board.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mypage/board")
@RequiredArgsConstructor
public class UserBoardController {

	@GetMapping("/list")
	public String list() {
		return "view/user/board/list";
	}
}
