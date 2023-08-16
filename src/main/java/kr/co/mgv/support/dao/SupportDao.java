package kr.co.mgv.support.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;

@Mapper
public interface SupportDao {

	// 지역, 극장 조회
	List<Theater> getTheatersByLocationNo(int locationNo);
	List<Location> getLocations();
	
}
