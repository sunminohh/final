package kr.co.mgv.schedule.service;

import org.springframework.stereotype.Service;

import kr.co.mgv.schedule.dao.ScheduleDao;
import kr.co.mgv.schedule.dto.DateWithMovieDto;
import kr.co.mgv.theater.dto.TheaterAndDateDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleDao scheduleDao;

	public DateWithMovieDto getScheduleList(TheaterAndDateDto dto) {
		return scheduleDao.getScheduleWithDate(dto);
	}
	
	
}
