package kr.co.mgv.booking.controller;

import kr.co.mgv.booking.service.BookingService;
import kr.co.mgv.schedule.dto.BookingScheduleDto;
import kr.co.mgv.schedule.service.ScheduleService;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/booking")
@AllArgsConstructor
public class BookingRestController {
    BookingService bookingService;
    @RequestMapping("/{date}")
    public Map<String, Integer> scheduleApi(@PathVariable String date){
    return bookingService.isElementClassActive(date);
    }
    @GetMapping("/selectedSchedule")
    public List<BookingScheduleDto> bookingScheduleApi(@RequestParam(value="tNames", required = false) String[] tNames,@RequestParam(value="mNos", required = false) String[] mNos,@RequestParam(value="date", required = false) String date){
        Map<String,String[]> map = new HashMap<>();
                map.put("tNames",tNames);
                map.put("mNos",mNos);
                map.put("date",new String[]{date});
        return  bookingService.getBookingSchedules(map);
    }


    @GetMapping("/step0")
    public Map<String, Object> xxx(@AuthenticationPrincipal User user, @RequestParam("schedulId") int scheduleId){
        Map<String, Object> map = new HashMap<>();
//        if (user == null) {
//            map.put("result", "fail");
//            map.put("scheduleId", scheduleId);
//            return map;
//        } else {
            map.put("result", "success");

            return map;
//        }
    }
}
