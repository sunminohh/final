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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/booking")
@AllArgsConstructor
@Slf4j
public class BookingRestController {
    private BookingService bookingService;
    private TheaterService theaterService;
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


    @GetMapping("/fromStep0toStep1")
    public Map<String, Object> fromStep0toStep1(@AuthenticationPrincipal User user, @RequestParam("scheduleId") int scheduleId){
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            map.put("result", "fail");
            map.put("scheduleId", scheduleId);
            return map;
        } else {
            map.put("result", "success");

            return map;
        }
    }

    @PostMapping("/updateSeats")
    public String updateSeats(@RequestBody List<String> params){
        theaterService.registDisalbedSeats(params);
        return "sucess";
    }
    @GetMapping("/getDisabledSeats")
    public Map<String,Object> getDisabledSeats(@RequestParam("screenId") int screenId){
        Map<String,Object> map = bookingService.getScreenMatrixByScreenId(screenId);
        map.put("disabledSeats",theaterService.getDisabledSeatsByScreenID(screenId));
        return map;
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
    public Map<String,Object> bookingPay(@RequestBody Booking booking, @AuthenticationPrincipal User user){
        Map<String,Object> map=new HashMap<>();
        if(user==null){
            map.put("result","fail");
            return map;
        }
            map.put("userId",user.getId());
            map.put("userName",user.getName());
            map.put("bookingNo",booking.getNo());
            booking.setUserId(user.getId());
            booking.setUserName(user.getName());
            if(booking.getPayAmount()==0){
                map.put("result","success");
                booking.setPayMethod("관람권");
            try{
                bookingService.insertBooking(booking);
                return map;
            }catch (Exception e){
                log.info(e.getMessage());
                map.put("result","fail");
                map.put("error",e.getMessage());
                return map;
            }
        }else{
                if(booking.getGiftAmount()>0){
                    booking.setPayMethod(booking.getPayMethod()+", 관람권");
                }
            map.put("result","pending");
            bookingService.insertBooking(booking);
            return map;
        }


        }

        @GetMapping("/getBookedSeats")
        public List<String> getBookedSeats(@RequestParam("scheduleId") int scheduleId){
            return bookingService.getBookedSeatsByScheduleId(scheduleId);
        }
        @GetMapping("insertBookedSeats")
        public String insertBookedSeats(@RequestParam("seatNos") String seatNos, @RequestParam("scheduleId") int scheduleId){
            List<String> params= Arrays.stream(seatNos.split(",")).collect(Collectors.toList());
            params.add(0, scheduleId+"");
            bookingService.insertBookedSeats(params);
            return "success";
        }
    @GetMapping("deleteBookedSeats")
    public String deleteBookedSeats(@RequestParam("seatNos") String seatNos, @RequestParam("scheduleId") int scheduleId){
        List<String> params= Arrays.stream(seatNos.split(",")).collect(Collectors.toList());
        params.add(0, scheduleId+"");
        bookingService.deleteBookedSeats(params);
        return "success";
    }
    
    @GetMapping("/getBookingList")
    public List<Booking> getBookingList(){
    	return bookingService.getBookingList();
    }
}
