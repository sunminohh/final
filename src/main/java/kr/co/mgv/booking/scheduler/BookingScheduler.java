package kr.co.mgv.booking.scheduler;

import kr.co.mgv.booking.dao.BookingDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class BookingScheduler {
        private BookingDao bookingDao;

        @Scheduled(fixedDelay = 5000)
        public void checkBookedSeatsTimeOut(){
            bookingDao.clearTimeOutBookedSeats();
        }
}
