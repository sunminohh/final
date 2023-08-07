package kr.co.mgv.common;

import kr.co.mgv.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

	@GetMapping("/")
	public String home(@AuthenticationPrincipal User user) {
		log.info("[HOME] - User: {}", user != null ? user.getUsername() : "Anonymous");
		return "view/index";
	}
}
