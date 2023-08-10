package kr.co.mgv.support.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.support.vo.One;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.theater.Theater;
import kr.co.mgv.theater.location.Location;

@Mapper
public interface OneDao {
	
	// 일대일문의글 등록
	void insertOne(One one);
	
	// 전체 카테고리 가져오기
	List<SupportCategory> getCategories(String categoryType);
	
	// 극장별문의에서 지역, 극장 셀렉트박스 가져오기
	List<Theater> getTheatersByLocationNo(int locationNo);
	List<Location> getLocations();
	
	
}
