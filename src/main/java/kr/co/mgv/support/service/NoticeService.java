package kr.co.mgv.support.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.support.dao.NoticeDao;
import kr.co.mgv.support.dao.SupportDao;
import kr.co.mgv.support.dto.NoticeList;
import kr.co.mgv.support.vo.Notice;
import kr.co.mgv.support.vo.SupportPagination;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticeService {

	private final NoticeDao noticeDao;
	private final SupportDao supportDao;
	
	public NoticeList search(Map<String, Object> param) {
		
		int totalRows = noticeDao.getTotalRows(param);
		int page = (int) param.get("page");
		
		SupportPagination pagination = new SupportPagination(page, totalRows);
		
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		List<Notice> noticeList = noticeDao.getNotices(param);
		NoticeList result = new NoticeList();
		
		result.setPagination(pagination);
		result.setNoticeList(noticeList);
		
		return result;
	}
	
	public List<Location> getloLocations() {
		return supportDao.getLocations();
	}
	
	public List<Theater> getTheatesrByLocationNo (int locationNo) {
		return supportDao.getTheatersByLocationNo(locationNo);
	}
	
}













