package kr.co.mgv.theater.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.theater.dto.FavoriteTheater;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;

@Mapper
public interface TheaterDao {

	List<Location> getTheaters();
	Theater getTheaterDetailByNo(int theaterNo);
	void updateTheater(Theater theater);
	void insertTheater(Theater theater);
	List<Theater> getFavoriteTheaters(String userId);
	void deleteFavoriteTheaters(String userId);
	void insertFavoriteTheaters(FavoriteTheater theater);
}
