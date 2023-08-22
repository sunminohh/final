package kr.co.mgv.booking.controller;

import kr.co.mgv.schedule.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/booking")
@AllArgsConstructor
public class BookingRestController {
    ScheduleService scheduleService;
    @RequestMapping("/{date}")
    public Map<String, String> scheduleApi(@PathVariable String date){
    return null;
    }
}
