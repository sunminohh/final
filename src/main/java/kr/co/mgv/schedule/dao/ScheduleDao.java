package kr.co.mgv.schedule.dao;

import kr.co.mgv.schedule.dto.BookingScheduleDto;
import kr.co.mgv.schedule.dto.DailyScheduleDto;
import kr.co.mgv.schedule.vo.Schedule;
import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.schedule.dto.CheckScheduleDto;
import kr.co.mgv.schedule.dto.DateWithMovieDto;
import kr.co.mgv.theater.dto.TheaterAndDateDto;

import java.util.List;
import java.util.Map;


@Mapper
public interface ScheduleDao {
	
	DateWithMovieDto getScheduleWithDate(TheaterAndDateDto dto);
	CheckScheduleDto checkSceduleByTheaterno(int theaterNo);
	List<DailyScheduleDto> getSchedulesByDate(String date);

	List<BookingScheduleDto> getBookingSchedules(Map<String,String> params);

}
