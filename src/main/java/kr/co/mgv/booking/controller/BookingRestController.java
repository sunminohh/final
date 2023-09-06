package kr.co.mgv.booking.controller;

import kr.co.mgv.booking.service.BookingService;
import kr.co.mgv.booking.vo.Booking;
import kr.co.mgv.schedule.dto.BookingScheduleDto;
import kr.co.mgv.theater.dto.SeatsDto;
import kr.co.mgv.theater.service.TheaterService;
import kr.co.mgv.theater.vo.DisabledSeat;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/booking")
@AllArgsConstructor
@Slf4j
public class BookingRestController {
    BookingService bookingService;
    TheaterService theaterService;
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

    @PostMapping("/updateSeats")
    public String updateSeats(@RequestBody List<String> params){
        theaterService.registDisalbedSeats(params);
        return "sucess";
    }
    @GetMapping("/getDisabledSeats")
    public List<String> getDisabledSeats(@RequestParam("screenId") int screenId){
        return theaterService.getDisabledSeatsByScreenID(screenId);
    }

    @GetMapping("/deleteDisabledSeats")
    public String deleteDisabledSeatsByScreenId(@RequestParam("screenId") int screenId){
        theaterService.deleteDisabledSeatsByScreenId(screenId);
        return "success";
    }
    @GetMapping("/getScreenMatrix")
        public Map<String,Integer> getScreenMatrix(@RequestParam("screenId") int screenId){

            return null;
        }
        @PostMapping("/bookingPay")
    public ResponseEntity<Booking> bookingPay(@RequestBody Booking booking, @AuthenticationPrincipal User user){
        booking.setUserId(user.getId());
        booking.setUserName(user.getName());
        try{
            bookingService.insertBooking(booking);
            return ResponseEntity.ok(booking);
        }catch (Exception e){
            log.info(e.getMessage());
            return (ResponseEntity<Booking>) ResponseEntity.badRequest();
        }

        }
}
