package kr.co.mgv.support.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.support.vo.One;
import kr.co.mgv.support.vo.OneComment;
import kr.co.mgv.support.vo.OneFile;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;

@Mapper
public interface OneDao {
	
	// 답변 조회
	List<OneComment> getOneCommentsByOne(int oneNo);
	// 답변 하나 조회
	OneComment getOneCommentByNo(int commentNo);
	// 답변 등록
	void insertComment(OneComment oneComment);
	// 답변 삭제
	void deleteComment(OneComment oneComment);
	
	// 일대일문의글 등록
	void insertOne(One one);
	
	// 유저 문의내역 조회
	List<One> getOnes(Map<String, Object> param);
	int getTotalRows(Map<String, Object> param);
	One getOneByNo(int oneNo);
	
	// 전체 카테고리 조회
	List<SupportCategory> getCategories(String categoryType);
	
	// 극장별문의에서 지역, 극장 조회
	List<Theater> getTheatersByLocationNo(int locationNo);
	List<Location> getLocations();
	
	// 일대일 문의글 업데이트
	void updateOneByNo(One one);
	
	// 첨부파일 정보 저장
	void insertOneFile(OneFile onefile);
	// 일대일문의글 번호로 첨부파일 조회
	List<OneFile> getOneFileByOneNo(int oneNo);
	// 파일번호로 파일조회
	OneFile getOneFileByFileNo(int fileNo);
}
