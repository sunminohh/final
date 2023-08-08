package kr.co.mgv.theater;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.theater.location.Location;

@Mapper
public interface TheaterDao {

	List<Location> getTheaters();
	Theater getTheaterDetailByNo(int theaterNo);
	void updateTheater(Theater theater);
	void insertTheater(Theater theater);
	
}
