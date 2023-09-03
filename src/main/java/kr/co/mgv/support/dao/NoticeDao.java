package kr.co.mgv.support.dao;

import java.util.List;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.support.vo.Notice;

@Mapper
public interface NoticeDao {
	
	// 공지사항 등록
	void insertNotice(Notice notice);

	// 공지사항 리스트 조회
	int getTotalRows(Map<String, Object> param);
	List<Notice> getNotice(Map<String, Object> param);
	List<Notice> getNoticeList();
	
	// 공지사항 업데이트
	void updateNoticeByNo(Notice notice);
	
	// 공지사항 디테일 조회
	Notice getNoticeByNo(int noticeNo);
	
	// 이전글가기
	Notice getPrevNotice(int noticeNo);
	// 다음글가기
	Notice getNextNotice(int noticeNo);
	
	
}
