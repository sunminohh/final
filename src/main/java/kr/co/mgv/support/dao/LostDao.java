package kr.co.mgv.support.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;

@Mapper
public interface LostDao {

	// 분실물문의글 등록
	void insertLost(Lost lost);
	
	// 분실장소에서 지역, 극장 가져오기
	List<Theater> getTheatersByLocationNo(int locationNo);
	List<Location> getLocations();
	
}
