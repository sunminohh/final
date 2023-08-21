package kr.co.mgv.admin.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.theater.service.TheaterService;
import kr.co.mgv.theater.vo.Theater;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/theater")
@RequiredArgsConstructor
public class AdminTheaterController {

	private final TheaterService theaterService;
	
    @GetMapping("/list")
    public String list() {
        return "/view/admin/theater/list";
    }
    
    @GetMapping("/register")
    public String form() {
        return "/view/admin/theater/schedule/form";
    }
    
    @GetMapping("/detail")
    @ResponseBody
    public Theater getTheaterDetail(@RequestParam int theaterNo) {
     	Theater theater = theaterService.getTheaterDetail(theaterNo);
     	return theater;
    }
}
