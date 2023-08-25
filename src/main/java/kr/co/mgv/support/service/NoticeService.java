package kr.co.mgv.support.service;

import java.util.List;

import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.support.dao.NoticeDao;
import kr.co.mgv.support.dao.SupportDao;
import kr.co.mgv.support.dto.NoticeList;
import kr.co.mgv.support.form.AddNoticeForm;
import kr.co.mgv.support.vo.Notice;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.support.vo.SupportPagination;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticeService {

	private final NoticeDao noticeDao;
	private final SupportDao supportDao;
	
	public void insertNotice(AddNoticeForm form, User user) {
		
		Location location = null;
		if (form.getLocationNo() != null) {
				location = Location.builder()
					.no(form.getLocationNo())
					.build();
	    }
		
		Theater theater = null;
	    if (form.getTheaterNo() != null) {
	    	  	theater = Theater.builder()
						.no(form.getTheaterNo())
						.build();
	    }

	    SupportCategory category = SupportCategory.builder()
							.no(form.getCategoryNo())
							.build();
	    
	    Notice notice = Notice.builder()
	    					.user(user)
	    					.location(location)
	    					.theater(theater)
	    					.category(category)
	    					.type(form.getNoticeType())
	    					.title(form.getTitle())
	    					.content(form.getContent())
	    					.build();
	    
	    noticeDao.insertNotice(notice);
	}
	
	public void deleteNotice(int noticeNo) {
		Notice notice = noticeDao.getNoticeByNo(noticeNo);
		notice.setDeleted("Y");
		
		noticeDao.updateNoticeByNo(notice);
	}
	
	public NoticeList search(Map<String, Object> param) {
		
		int totalRows = noticeDao.getTotalRows(param);
		int page = (int) param.get("page");
		
		SupportPagination pagination = new SupportPagination(page, totalRows);
		
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		List<Notice> noticeList = noticeDao.getNotice(param);
		NoticeList result = new NoticeList();
		
		result.setPagination(pagination);
		result.setNoticeList(noticeList);
		
		return result;
	}
	
	public Notice getNoticeByNo(int noticeNo) {
		return noticeDao.getNoticeByNo(noticeNo);
	}
	
	public Notice getPrevNotice(int noticeNo) {
		return noticeDao.getPrevNotice(noticeNo);
	}
	
	public Notice getNextNotice(int noticeNo) {
		return noticeDao.getNextNotice(noticeNo);
	}
	
	public List<Location> getloLocations() {
		return supportDao.getLocations();
	}
	
	public List<Theater> getTheatesrByLocationNo (int locationNo) {
		return supportDao.getTheatersByLocationNo(locationNo);
	}
	
	
	
}













