package kr.co.mgv.theater.dao;

import java.util.List;
import java.util.Map;

import kr.co.mgv.theater.vo.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TheaterDao {

	List<Location> getTheaters();
	List<Theater> getTheatersByLocationNo(int locationNo);
	Theater getTheaterDetailByNo(int theaterNo);
	void updateTheater(Theater theater);
	void insertTheater(Theater theater);
	
	List<Screen> getScreensByTheaterNo(int theaterNo);
	Screen getScreenById(int screenId);
	
	List<TheaterFacility> getFacilities();
	void insertFacilityInfo(TheaterFacility facility);
	
	void insertFloorInfo(FloorInfo floorInfo);
	
	void insertParkingInfo(ParkingInfo parkingInfo);

	void insertDisabledSeats(Map<String,Object> params);

	List<String> getDisabledSeatsByScreenId(int screenId);
}
