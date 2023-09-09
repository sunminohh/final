package kr.co.mgv.booking.dao;

import kr.co.mgv.booking.vo.Booking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookingDao {
    public void insertBooking(Booking booking);
    public Booking getBookingByBookingNo(long no);
    public List<Booking> getBookings();
    public void updateBooking(Booking Booking);
    void insertBookedSeats(Map<String,Object> params);
    List<String> getBookedSeatsByScheduleId(int scheduleId);
    void completeBookedSeats(Map<String,Object> params);
    void deleteBookedSeats(Map<String,Object> params);
    void clearTimeOutBookedSeats();
}
