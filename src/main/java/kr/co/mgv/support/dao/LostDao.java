package kr.co.mgv.support.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.support.vo.LostFile;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;

@Mapper
public interface LostDao {

	// 분실물문의 등록
	void insertLost(Lost lost);
	
	// 분실장소에서 지역, 극장 조회
	List<Theater> getTheatersByLocationNo(int locationNo);
	List<Location> getLocations();
	
	// 분실물문의 리스트 조회
	List<Lost> getlosts(Map<String, Object> param);
	int getTotalRows(Map<String, Object> param);
	
	// 분실물문의 디테일 조회
	Lost getLostByNo (int lostNo);
	
	// 분실물 문의글 업데이트
	void updateLostByNo(Lost lost);
	
	// 첨부파일 정보 저장
	void insertLostFile(LostFile lostFile);
	// 분실물문의글 번호로 첨부파일 조회
	List<LostFile> getLostFilesByLostNo(int lostNo);
	// 파일번호로 파일조회
	LostFile getLostFileByFileNo(int fileNo);
	
}













