package kr.co.mgv.booking.dao;

import kr.co.mgv.booking.vo.Booking;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookingDao {
    public void insertBooking(Booking booking);
}
