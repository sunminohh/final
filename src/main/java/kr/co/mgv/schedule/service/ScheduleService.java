package kr.co.mgv.schedule.service;

import kr.co.mgv.movie.dao.MovieDao;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.schedule.dao.ScheduleDao;
import kr.co.mgv.schedule.dto.CheckScheduleDto;
import kr.co.mgv.schedule.dto.DateWithMovieDto;
import kr.co.mgv.schedule.dto.ScheduleRegistFormDto;
import kr.co.mgv.schedule.vo.Schedule;
import kr.co.mgv.theater.dao.TheaterDao;
import kr.co.mgv.theater.dto.TheaterAndDateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleDao scheduleDao;
	private final TheaterDao theaterDao;
	private final MovieDao movieDao;


	public DateWithMovieDto getScheduleList(TheaterAndDateDto dto) {
		return scheduleDao.getScheduleWithDate(dto);
	}

	public CheckScheduleDto checkSchedule(int theaterNo) {
	
		return scheduleDao.checkSceduleByTheaterno(theaterNo);
	}

	public List<Map<String, Object>> getScheduleList(String date, int screen) {
		Map<String, Object> map = new HashMap<>();
		map.put("date", date);
		map.put("screenId", screen);
		return scheduleDao.getSchedulesByScreenAndDate(map);
	}

	public String registSchedule(ScheduleRegistFormDto dto) {
		log.info("dto->{}",dto);
		try {
			dto.setSeats(theaterDao.getScreenById(dto.getScreenId()).getSeats());
			Schedule schedule= scheduleDao.getSchedule(dto);
			if(schedule != null) {
				return "timeduplicated";
			}
			scheduleDao.insertSchedule(dto);
			Movie movie= movieDao.getMovieByMovieNo(dto.getMovieNo());
			movie.setIsPlaying("Y");
			movie.setSeatsOpen(dto.getSeats()+movie.getSeatsOpen());
			movieDao.updateMovie(movie);
		}catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	public void deleteSchedule(String[] idList) {
		int scheduleId =0;
		for(String id : idList) {
			scheduleId = Integer.parseInt(id);
			scheduleDao.deleteSchedule(scheduleId);
		}
	}
	
	
	
	
}
