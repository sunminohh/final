package kr.co.mgv.common;

import kr.co.mgv.board.list.BoardList;
import kr.co.mgv.board.service.MyBoardService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final MyBoardService myBoardService;

	@GetMapping("/")
	public String home(@AuthenticationPrincipal User user, Model model) {
		log.info("[HOME] - User: {}", user != null ? user.getUsername() : "Anonymous");
		
		List<BoardList> commentList = myBoardService.getBest5("comment");
		List<BoardList> likeList = myBoardService.getBest5("like");
		
		model.addAttribute("like", likeList);
		model.addAttribute("comment", commentList);
		return "view/index";
	}
}
