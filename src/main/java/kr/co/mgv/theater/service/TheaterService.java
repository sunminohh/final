package kr.co.mgv.theater.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mgv.theater.dao.TheaterDao;
import kr.co.mgv.theater.dto.FavoriteTheater;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
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

	public List<Theater> getFavoriteTheaters(String userId) {
		return theaterDao.getFavoriteTheaters(userId);
	}

	public void registFavoriteTheater(String userId, List<FavoriteTheater> favoriteTheaters) {
		theaterDao.deleteFavoriteTheaters(userId);
		for(FavoriteTheater theater: favoriteTheaters) {
			theater.setUserId(userId);
			if(theater.getTheaterNo() != 0) {
				theaterDao.insertFavoriteTheaters(theater);
			}
		}
	}
}
