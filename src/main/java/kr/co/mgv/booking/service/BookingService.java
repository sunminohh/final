package kr.co.mgv.booking.service;

import kr.co.mgv.schedule.dao.ScheduleDao;
import kr.co.mgv.schedule.dto.BookingScheduleDto;
import kr.co.mgv.schedule.dto.DailyScheduleDto;
import kr.co.mgv.theater.service.TheaterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class BookingService {

        ScheduleDao scheduleDao;
        TheaterService theaterService;

        public Map<String, Integer> isElementClassActive(String date){
            Map<String,Integer> map= new HashMap<>();

            List<DailyScheduleDto> dailyScheduleDtos =scheduleDao.getSchedulesByDate(date);
            dailyScheduleDtos.forEach(ds->{
               map.put(String.valueOf(ds.getMovieNo()),1);
               map.put(String.valueOf(ds.getTheaterNo()),1);
            });
            return map;
        }

        public List<BookingScheduleDto> getBookingSchedules(Map<String, String[]> params){
            return scheduleDao.getBookingSchedules(params);
        }


}
