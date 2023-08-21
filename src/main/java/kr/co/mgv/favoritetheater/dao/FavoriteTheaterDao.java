package kr.co.mgv.favoritetheater.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.favoritetheater.vo.FavoriteTheater;
import kr.co.mgv.theater.vo.Theater;

@Mapper
public interface FavoriteTheaterDao {

	List<Theater> getFavoriteTheaters(String userId);
	FavoriteTheater getFavoriteTheater(FavoriteTheater favoriteTheater);
	void deleteFavoriteTheaters(String userId);
	void insertFavoriteTheaters(FavoriteTheater theater);
	void deleteFavoriteTheater(FavoriteTheater favoriteTheater);
}
