package kr.co.mgv.booking.controller;

import kr.co.mgv.booking.service.BookingService;
import kr.co.mgv.booking.vo.Booking;
import kr.co.mgv.movie.service.MovieService;
import kr.co.mgv.theater.service.TheaterService;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import lombok.AllArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/booking")
@AllArgsConstructor
public class BookingController {

   private MovieService movieService;
   private TheaterService theaterService;
   private BookingService bookingService;
    @RequestMapping({"/", ""})
    public String home(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
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

        model.addAttribute("booking", booking);
        booking.setBookingState("결제 완료");
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
