package kr.co.mgv.booking.controller;

import kr.co.mgv.booking.service.BookingService;
import kr.co.mgv.schedule.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/booking")
@AllArgsConstructor
public class BookingRestController {
    BookingService bookingService;
    @RequestMapping("/{date}")
    public Map<String, Integer> scheduleApi(@PathVariable String date){
    return bookingService.isElementClassActive(date);
    }


}
