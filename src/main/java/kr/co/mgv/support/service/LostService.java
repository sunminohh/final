package kr.co.mgv.support.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.support.dao.LostDao;
import kr.co.mgv.support.dto.LostList;
import kr.co.mgv.support.form.AddLostForm;
import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.support.vo.SupportPagination;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LostService {

	private final LostDao lostDao;
	
	public void insertLost(AddLostForm form, User user) {
		
		Location location = Location.builder()
							.no(form.getLocationNo())
							.build();
		
		Theater theater = Theater.builder()
							.no(form.getTheaterNo())
							.build();
		Lost lost = null;
		if (user != null) {
			lost = Lost.builder()
					.user(user) // If user is not null
					.location(location)
					.theater(theater)
					.title(form.getTitle())
					.content(form.getContent())
					.build();
			
		} else {
			lost = Lost.builder()
					.guestName(form.getGuestName()) // If user is null
					.guestEmail(form.getGuestEmail()) // If user is null
					.guestPassword(form.getGuestPassword()) // If user is null
					.location(location)
					.theater(theater)
					.title(form.getTitle())
					.content(form.getContent())
					.build();
		}
		

		lostDao.insertLost(lost);
	}
	

	public LostList search(Map<String, Object> param) {
		log.info("param -> {}", param);
		int totalRows = lostDao.getTotalRows(param);
		int page = (int) param.get("page");
		
		SupportPagination pagination = new SupportPagination(page, totalRows);
		
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		List<Lost> lostList = lostDao.getlosts(param);
		LostList result = new LostList();
		
		result.setPagination(pagination);;
		result.setLostList(lostList);
		
		return result;
	}
	
	public Lost getLostByNo(int lostNo) {
		
		return lostDao.getLostByNo(lostNo);
	}
	
	public List<Location> getLocations() {
		return lostDao.getLocations();
	}
	
	public List<Theater> getTheatesrByLocationNo(int locationNo) {
		return lostDao.getTheatersByLocationNo(locationNo);
	}
}

















