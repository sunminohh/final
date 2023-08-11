package kr.co.mgv.schedule.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.schedule.dto.DateWithMovieDto;
import kr.co.mgv.theater.dto.TheaterAndDateDto;


@Mapper
public interface ScheduleDao {
	
	DateWithMovieDto getScheduleWithDate(TheaterAndDateDto dto);
}
