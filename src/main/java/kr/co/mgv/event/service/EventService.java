package kr.co.mgv.event.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mgv.common.file.FileUtils;
import kr.co.mgv.event.dao.EventDao;
import kr.co.mgv.event.form.AddEventForm;
import kr.co.mgv.event.vo.Event;
import kr.co.mgv.event.vo.EventCategory;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventService {

	private final EventDao eventDao;
	private final FileUtils fileUtils;
	
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
	
	public List<EventCategory> getCategories() {
		return eventDao.getCategories();
	}
	
}









