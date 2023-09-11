package kr.co.mgv.movie.controller;

import kr.co.mgv.movie.service.MovieService;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.movie.vo.MovieLike;
import kr.co.mgv.user.service.UserService;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

@Controller
@RequestMapping("/movie")
@AllArgsConstructor
@Slf4j
public class MovieController {

    @Autowired
    private final MovieService movieService;
    private UserService userService;
    @RequestMapping({"/", ""})
    public String home(Model model,  @AuthenticationPrincipal User user ) {
        List<Movie> movies = movieService.getMovieChart();
        model.addAttribute("movies", movies);
        if(user!=null) {
            model.addAttribute("likedMovies", movieService.getAllLikedMovieNos(user.getId()));
        }
        return "view/movie/list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("movieNo") int movieNo, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("movie",movieService.getMovieByMovieNo(movieNo));
        model.addAttribute("movieComment",movieService.getMovieCommentsByMovieNo(movieNo));

        if(user!=null){
            model.addAttribute("user",userService.getUserById(user.getId()));
            model.addAttribute("isLiked",movieService.isMovieLikedByUser(new MovieLike(user.getId(),movieNo)));
        }else model.addAttribute("isLiked",false);

        return "view/movie/detail";
    }

    @GetMapping("/post")
    public String moviePost() {
        return "view/movie/post";
    }

    @GetMapping("/test")
    public void test(){
        List<Movie> movies = movieService.getMovies();
    return;
    }

@GetMapping("/poster")
   public ModelAndView download(@RequestParam ("url") String url, @RequestParam("filename") String filename, Model model) {
    model.addAttribute("filename",filename);
    model.addAttribute("url",url);
    return new ModelAndView("downloadFileView");
}

@GetMapping("/favorite")
public String movieFavorite (Model model, @AuthenticationPrincipal User user){
    if(user!= null){
        model.addAttribute("favoriteMovies",movieService.getFavoriteMoviesByUserId(user.getId()));
        }else{
            model.addAttribute("favoriteMovies",new ArrayList<>());

        }
        return "view/movie/favorite";
}
@GetMapping("/movieall")
public String movieAll(Model model, @AuthenticationPrincipal User user){
    model.addAttribute("movies",movieService.getAllMovies());
    if(user!=null) {
        model.addAttribute("likedMovies", movieService.getAllLikedMovieNos(user.getId()));
    }
    return "view/movie/movieall";
}
    @GetMapping("/sync")
    public String sync(){
        movieService.sync();
        return "list";
    }


}
