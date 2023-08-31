package kr.co.mgv.theater.service;

import java.util.*;
import java.util.stream.Collectors;

import kr.co.mgv.theater.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import kr.co.mgv.favoritetheater.dao.FavoriteTheaterDao;
import kr.co.mgv.favoritetheater.vo.FavoriteTheater;
import kr.co.mgv.theater.dao.TheaterDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	public void registDisalbedSeats(List<String> seatNos){
		int screenId=Integer.parseInt(seatNos.get(0));
		seatNos.remove(0);
		HashSet<String> overLap = new HashSet<>(theaterDao.getDisabledSeatsByScreenId(screenId));
		Map<String,Object> params=new HashMap<>();
		params.put("screenId",screenId);
		params.put("seatNos",seatNos.stream().filter(seatNo-> !overLap.contains(seatNo)).collect(Collectors.toList()));
		theaterDao.insertDisabledSeats(params);
	}

	public List<String> getDisabledSeatsByScreenID(int ScreenId){
		return theaterDao.getDisabledSeatsByScreenId(ScreenId);
	}


	public void modifyTheater(Theater theater) {
		// 업데이트전 확인
		Theater pretheater = theaterDao.getTheaterDetailByNo(theater.getNo());
		
		BeanUtils.copyProperties(theater, pretheater);
		theaterDao.updateTheater(pretheater);
		
		
		theaterDao.deleteParkingInfo(pretheater.getNo());
		pretheater.getParkingInfo().setTheaterNo(pretheater.getNo());
		theaterDao.insertParkingInfo(pretheater.getParkingInfo());
		
		theaterDao.deleteFacilityInfo(pretheater.getNo());
		for(TheaterFacility facility : pretheater.getFacilities()) {
			facility.setTheaterNo(pretheater.getNo());
			theaterDao.insertFacilityInfo(facility);
		}
		
		theaterDao.deleteFloorInfo(pretheater.getNo());
		for(FloorInfo floorInfo : pretheater.getFloorInfos()) {
			floorInfo.setTheaterNo(pretheater.getNo());
			theaterDao.insertFloorInfo(floorInfo);
		}
	}

	public void deleteDisabledSeatsByScreenId(int screenId){
		theaterDao.deleteDisabledSeatsByScreenId(screenId);
	}
}
