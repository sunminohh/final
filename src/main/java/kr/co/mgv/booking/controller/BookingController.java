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

        return "/view/booking/home";
    }

}
