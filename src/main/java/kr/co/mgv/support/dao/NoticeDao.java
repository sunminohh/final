package kr.co.mgv.support.dao;

import java.util.List;
import java.util.Map;

import kr.co.mgv.support.vo.Notice;

public interface NoticeDao {

	// 공지사항 리스트 조회
	List<Notice> getNotices(Map<String, Object> param);
	int getTotalRows(Map<String, Object> param);
	
	// 공지사항 디테일 조회
	
}
