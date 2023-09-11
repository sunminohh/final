package kr.co.mgv.booking.controller;

import kr.co.mgv.booking.service.BookingService;
import kr.co.mgv.booking.vo.Booking;
import kr.co.mgv.movie.dao.MovieDao;
import kr.co.mgv.movie.service.MovieService;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.theater.service.TheaterService;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.service.UserService;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/booking")
@AllArgsConstructor
public class BookingController {

   private MovieService movieService;
   private TheaterService theaterService;
   private BookingService bookingService;
   private UserService userService;
   private MovieDao movieDao;
    @RequestMapping({"/", ""})
    public String home(@RequestParam(value="fail",required = false)String fail, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("fail",fail);
            List<Movie> movies =  movieService.getAllMovies();
        if(user!=null){
            HashSet<Integer> set = movieService.getAllLikedMovieNos(user.getId());
           movies.forEach(movie -> movie.setLiked(set.contains(movie.getNo())));
        }
        model.addAttribute("movies",movies);
        model.addAttribute("locations", theaterService.getTheaters());
        return "view/booking/home";
    }

    @GetMapping({"/success"})
    public String success(@RequestParam("orderId") long bookingNo, @RequestParam(required = false, value="amount") Long amount, @RequestParam(required = false, value="paymentKey") String paymentKey, Model model) {
        Booking booking = bookingService.getBookingByBookingNo(bookingNo);
        if(paymentKey!=null){
            ResponseEntity<String> tossResponseEntity= bookingService.requestTossFinalPayment(bookingNo+"",paymentKey,amount);
            booking.setPaymentKey(paymentKey);
        }
        Movie movie = movieService.getMovieByMovieNo(booking.getMovieNo());
        movie.setSeatsBooked(booking.getTotalSeats());
        movieDao.updateMovie(movie);
        bookingService.completeBookedSeats(booking);
        model.addAttribute("booking", booking);
        booking.setBookingState("결제완료");
        bookingService.updateBooking(booking);
        return "view/booking/success";
    }
    @GetMapping({"/failure"})
    public String failure(@RequestParam("orderId") long bookingNo,Model model) {
        Booking booking = bookingService.getBookingByBookingNo(bookingNo);
        model.addAttribute("booking", booking);
        return "view/booking/failure";
    }


}
