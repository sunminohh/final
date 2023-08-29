package kr.co.mgv.theater.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mgv.favoritetheater.dao.FavoriteTheaterDao;
import kr.co.mgv.favoritetheater.vo.FavoriteTheater;
import kr.co.mgv.theater.dao.TheaterDao;
import kr.co.mgv.theater.vo.FloorInfo;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Screen;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.theater.vo.TheaterFacility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TheaterService {
	
	private final TheaterDao theaterDao;
	private final FavoriteTheaterDao favoriteDao;
	
	public List<Location> getTheaters() {
		return theaterDao.getTheaters();
	}
	
	public Theater getTheaterDetail(int theaterNo) {
		return theaterDao.getTheaterDetailByNo(theaterNo);
	}

	public List<Theater> getFavoriteTheaters(String userId) {
		return favoriteDao.getFavoriteTheaters(userId);
	}

	public void registFavoriteTheaters(String userId, List<FavoriteTheater> favoriteTheaters) {
		favoriteDao.deleteFavoriteTheaters(userId);
		for(FavoriteTheater theater: favoriteTheaters) {
			theater.setUserId(userId);
			if(theater.getTheaterNo() != 0) {
				favoriteDao.insertFavoriteTheaters(theater);
			}
		}
	}

	public void registFavoriteTheater(String userId, FavoriteTheater favoriteTheater) {
		favoriteTheater.setUserId(userId);
		
		FavoriteTheater theater = favoriteDao.getFavoriteTheater(favoriteTheater);
		log.info("선호극장정보 ->{}",theater);
		if(theater != null) {
			favoriteDao.deleteFavoriteTheater(favoriteTheater);
		}else {
			favoriteDao.insertFavoriteTheaters(favoriteTheater);
		}
		
	}

	public List<Theater> getTheaterlist(int locationNo) {
		List<Theater> list= theaterDao.getTheatersByLocationNo(locationNo);
		return list;
	}

	public List<Screen> getScreenlist(int theaterNo) {
		List<Screen> screens = theaterDao.getScreensByTheaterNo(theaterNo);
		return screens;
	}

	public List<TheaterFacility> getFacilities() {
		
		return theaterDao.getFacilities();
	}

	public void registTheater(Theater theater) {
		theaterDao.insertTheater(theater);
		theater.getParkingInfo().setTheaterNo(theater.getNo());
		theaterDao.insertParkingInfo(theater.getParkingInfo());
		
		for(TheaterFacility facility: theater.getFacilities()) {
			facility.setTheaterNo(theater.getNo());
			theaterDao.insertFacilityInfo(facility);
		}
		
		for(FloorInfo floorInfo : theater.getFloorInfos()) {
			floorInfo.setTheaterNo(theater.getNo());
			theaterDao.insertFloorInfo(floorInfo);
		}
	}
}
