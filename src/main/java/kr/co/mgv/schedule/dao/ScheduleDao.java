package kr.co.mgv.schedule.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.schedule.dto.CheckScheduleDto;
import kr.co.mgv.schedule.dto.DateWithMovieDto;
import kr.co.mgv.schedule.dto.ScheduleRegistFormDto;
import kr.co.mgv.schedule.vo.Schedule;
import kr.co.mgv.theater.dto.TheaterAndDateDto;


@Mapper
public interface ScheduleDao {
	
	DateWithMovieDto getScheduleWithDate(TheaterAndDateDto dto);
	CheckScheduleDto checkSceduleByTheaterno(int theaterNo);
	List<Map<String, Object>> getSchedulesByScreenAndDate(Map<String, Object> map);
	void insertSchedule(ScheduleRegistFormDto dto);
	Schedule getSchedule(ScheduleRegistFormDto dto);
	void deleteSchedule(int id);
}
