package kr.co.mgv.booking.service;

import kr.co.mgv.booking.dao.BookingDao;
import kr.co.mgv.booking.vo.Booking;
import kr.co.mgv.schedule.dao.ScheduleDao;
import kr.co.mgv.schedule.dto.BookingScheduleDto;
import kr.co.mgv.schedule.dto.DailyScheduleDto;
import kr.co.mgv.theater.dao.TheaterDao;
import kr.co.mgv.theater.service.TheaterService;
import kr.co.mgv.theater.vo.Screen;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class BookingService {

       private TheaterDao theaterDao;
       private ScheduleDao scheduleDao;
       private TheaterService theaterService;
       private BookingDao bookingDao;
    private static final JSONParser JSON_PARSER = new JSONParser();
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


        public Map<String, Object> getScreenMatrixByScreenId(int screenId){
            Screen screen = theaterDao.getScreenById(screenId);
            Map<String,Object> map= new HashMap<>();
            map.put("row",screen.getScreenRow());
            map.put("col",screen.getScreenCol());
            return map;
        }
        public void insertBooking(Booking booking){
            bookingDao.insertBooking(booking);
        }
        public Booking getBookingByBookingNo(long no){
           return bookingDao.getBookingByBookingNo(no);
        }

        public  ResponseEntity<String> requestTossFinalPayment(String orderId, String paymentKey, Long amount){
            String testSK="test_sk_E92LAa5PVbpv4lOOMGJ87YmpXyJj:";
            RestTemplate rest= new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            String auth= new String(Base64.getEncoder().encode(testSK.getBytes(StandardCharsets.UTF_8)));
            headers.setBasicAuth(auth);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            JSONObject params= new JSONObject();
            params.put("orderId",orderId);
            params.put("amount",amount+"");
            HttpEntity entity= new HttpEntity<>(params,headers);
            log.info("토스페이먼츠 api 응답 -> {}",entity);
             ResponseEntity<String> result=rest.postForEntity("https://api.tosspayments.com/v1/payments/"+paymentKey,entity,String.class);
            return result;
        }

        public void updateBooking(Booking booking){
            bookingDao.updateBooking(booking);
        }
}
