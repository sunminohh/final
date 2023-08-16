package kr.co.mgv.support.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.support.dao.LostDao;
import kr.co.mgv.support.dao.OneDao;
import kr.co.mgv.support.dto.OneList;
import kr.co.mgv.support.form.AddOneForm;
import kr.co.mgv.support.vo.One;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.support.vo.SupportPagination;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OneService {
	
	private final OneDao oneDao;
	
	public void insertOne(AddOneForm form, User user) {
		
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
		
		One one = null;
		if (user != null) {
			one = One.builder()
					.user(user)
					.location(location)
					.theater(theater)
					.category(category)
					.title(form.getTitle())
					.content(form.getContent())
					.build();
		} else {
			one = One.builder()
					.guestName(form.getGuestName())
					.guestEmail(form.getGuestEmail())
					.guestPassword(form.getGuestPassword())
					.location(location)
					.theater(theater)
					.category(category)
					.title(form.getTitle())
					.content(form.getContent())
					.build();
		}
		
		
		oneDao.insertOne(one);
	}
	
	public OneList search(Map<String, Object> param) {
		int totalRows = oneDao.getTotalRows(param);
		int page = (int) param.get("page");
		
		SupportPagination pagination = new SupportPagination(page, totalRows);
		
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		List<One> oneList = oneDao.getOnesByUserId(param);
		OneList result = new OneList();
		
		result.setPagination(pagination);
		result.setOneList(oneList);
	
		return result;
	}

	public List<SupportCategory> getCategoriesByType(String categoryType) {
		
		return oneDao.getCategories(categoryType);
	}
	
	public List<Location> getLocations() {
		
		return oneDao.getLocations();
	}
	
	public List<Theater> getTheatesrByLocationNo(int locationNo) {
		
		return oneDao.getTheatersByLocationNo(locationNo);
	}

}










