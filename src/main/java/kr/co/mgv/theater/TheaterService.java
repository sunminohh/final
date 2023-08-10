package kr.co.mgv.theater;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mgv.theater.location.Location;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TheaterService {
	
	private final TheaterDao theaterDao;
	
	public List<Location> getTheaters() {
		return theaterDao.getTheaters();
	}
	
	public Theater getTheaterDetail(int theaterNo) {
		return theaterDao.getTheaterDetailByNo(theaterNo);
	}
}
