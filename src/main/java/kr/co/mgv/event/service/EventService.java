package kr.co.mgv.event.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.common.file.FileUtils;
import kr.co.mgv.event.dao.EventDao;
import kr.co.mgv.event.dto.EventList;
import kr.co.mgv.event.form.AddEventForm;
import kr.co.mgv.event.form.ModifyEventForm;
import kr.co.mgv.event.vo.Event;
import kr.co.mgv.event.vo.EventCategory;
import kr.co.mgv.support.vo.SupportPagination;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventService {

	private final EventDao eventDao;
	private final FileUtils fileUtils;
	
	public void modifyEvent(ModifyEventForm form, int eventNo) {
		
		Event event = eventDao.getEventByNo(eventNo);
		
		String saveMainImageFilename = fileUtils.saveFile("static/images/event", form.getFile1());
		String saveDetailImageFilename = fileUtils.saveFile("static/images/event", form.getFile2());
		
		event.setTitle(form.getTitle());
		event.setStartDate(form.getStartDate());
		event.setEndDate(form.getEndDate());
		event.getCategory().setNo(form.getCategoryNo());
		event.setMainImage(saveMainImageFilename);
		event.setDetailImage(saveDetailImageFilename);
		
		eventDao.updateEventByNo(event);
	}
	
	public void deleteEvent(int eventNo) {
		Event event = eventDao.getEventByNo(eventNo);
		event.setDeleted("Y");
		
		eventDao.updateEventByNo(event);
	}
	
	public void insertEvent(AddEventForm form, User user) {
		
		EventCategory category = EventCategory.builder()
								.no(form.getCategoryNo())
								.build();

		String saveMainImageFilename = fileUtils.saveFile("static/images/event", form.getFile1());
		String saveDetailImageFilename = fileUtils.saveFile("static/images/event", form.getFile2());
				
		
		Event event = Event.builder()
						.user(user)
						.category(category)
						.title(form.getTitle())
						.startDate(form.getStartDate())
						.endDate(form.getEndDate())
						.mainImage(saveMainImageFilename)
						.detailImage(saveDetailImageFilename)
						.build();
		
		eventDao.insertEvent(event);
	}
	
	public EventList search(Map<String, Object> param) {
		
		int totalRows = eventDao.getTotalRows(param);
		int page = (int) param.get("page");
		
		SupportPagination pagination = new SupportPagination(page, totalRows);
		
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		List<Event> eventList = eventDao.getEventList(param);
		EventList result = new EventList();
		
		result.setPagination(pagination);
		result.setEventList(eventList);
		
		return result;
	}
	
	public Event getEventByNo(int eventNo) {
		return eventDao.getEventByNo(eventNo);
	} 
	
	public List<EventCategory> getCategories() {
		return eventDao.getCategories();
	}
	
}









