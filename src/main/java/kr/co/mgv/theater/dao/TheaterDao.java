package kr.co.mgv.theater.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.theater.vo.FloorInfo;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.ParkingInfo;
import kr.co.mgv.theater.vo.Screen;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.theater.vo.TheaterFacility;

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
	void deleteFacilityInfo(int no);
	
	void insertFloorInfo(FloorInfo floorInfo);
	void deleteFloorInfo(int no);
	
	void insertParkingInfo(ParkingInfo parkingInfo);
	void deleteParkingInfo(int no);
}
