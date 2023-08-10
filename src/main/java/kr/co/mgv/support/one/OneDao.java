package kr.co.mgv.support.one;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.support.SupportCategory;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;

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
