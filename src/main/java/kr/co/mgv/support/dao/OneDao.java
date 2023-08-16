package kr.co.mgv.support.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.support.vo.One;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;

@Mapper
public interface OneDao {
	
	// 일대일문의글 등록
	void insertOne(One one);
	
	// 유저 문의내역 조회
	List<One> getOnesByUserId(Map<String, Object> param);
	int getTotalRows(Map<String, Object> param);
	
	// 전체 카테고리 조회
	List<SupportCategory> getCategories(String categoryType);
	
	// 극장별문의에서 지역, 극장 조회
	List<Theater> getTheatersByLocationNo(int locationNo);
	List<Location> getLocations();
	
	
}
