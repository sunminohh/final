package kr.co.mgv.support.one;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support/one")
@RequiredArgsConstructor
public class OneController {

	private final OneService oneService;
	
	@GetMapping()
    public String one() {
        return "/view/support/one/form";
    }
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/add")
	public String createOne(@AuthenticationPrincipal User user, AddOneForm form) {
		oneService.createOne(form);
		
		return "redirect:/support/one";
	}
	
	
}
