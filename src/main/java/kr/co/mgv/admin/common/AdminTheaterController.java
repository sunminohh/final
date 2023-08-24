package kr.co.mgv.admin.common;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.movie.service.MovieService;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.theater.service.TheaterService;
import kr.co.mgv.theater.vo.Screen;
import kr.co.mgv.theater.vo.Theater;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/theater")
@RequiredArgsConstructor
public class AdminTheaterController {

	private final TheaterService theaterService;
	private final MovieService movieService;
	
    @GetMapping("/list")
    public String list() {
        return "/view/admin/theater/list";
    }
    
    @GetMapping("/schedule/register")
    public String form() {
        return "/view/admin/theater/schedule/form";
    }
    
    @GetMapping("/schedule/delete")
    public String deleteForm() {
    	return "/view/admin/theater/schedule/deleteForm";
    }
    
    @GetMapping("/detail")
    @ResponseBody
    public Theater getTheaterDetail(@RequestParam int theaterNo) {
     	Theater theater = theaterService.getTheaterDetail(theaterNo);
     	return theater;
    }
    
    @GetMapping("/schedule/list")
    public String scheduleList(Model model,@RequestParam int theaterNo) {
    	model.addAttribute("theaterNo", theaterNo);
    	return "/view/admin/theater/schedule/list";
    }
    
    @GetMapping("/theaterList")
    @ResponseBody
    public List<Theater> getTheaters(@RequestParam int locationNo){
    	List<Theater> theaterList = theaterService.getTheaterlist(locationNo);
    	return theaterList;
    }
    
    @GetMapping("/screenList")
    @ResponseBody
    public List<Screen> getscreenList(@RequestParam int theaterNo){
    	List<Screen> screens = theaterService.getScreenlist(theaterNo);
    	return screens;
    }
    
    @GetMapping("/movieList")
    @ResponseBody
    public List<Movie> getMovieList(){
    	return movieService.getAllMovies();
    }
    
}
