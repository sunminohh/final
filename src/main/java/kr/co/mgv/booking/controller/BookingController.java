package kr.co.mgv.booking.controller;

import kr.co.mgv.movie.service.MovieService;
import kr.co.mgv.theater.service.TheaterService;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/booking")
@AllArgsConstructor
public class BookingController {

    MovieService movieService;
    TheaterService theaterService;
    @RequestMapping({"/", ""})
    public String home(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("locations", theaterService.getTheaters());
        Map<String,Object> map=new HashMap<>();
        map.put("서울",1);
        map.put("경기",2);
        map.put("인천",3);
        map.put("오펜하이머","disabled");
        map.put("서울극장","disabled");
        model.addAttribute("map",map);
        return "/view/booking/home";
    }

}
