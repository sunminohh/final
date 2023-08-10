package kr.co.mgv.support.one;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mgv.support.SupportCategory;
import kr.co.mgv.theater.location.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OneService {
	
	private final OneDao oneDao;

	public void createOne(AddOneForm form, User user) {
		One one = new One();
		
		if (user != null) {
			one.setUser(user);
		} else {
			one.setGuestName(form.getGuestName());
			one.setGuestEmail(form.getGuestEmail());
			one.setGuestPassword(form.getGuestPassword());
		}
		
		if (form.getLocationNo() != null) {
			one.setLocation(new Location(form.getLocationNo()));
		}
		if (form.getTheaterNo() != null) {
			one.setTheater(new Theater(form.getTheaterNo()));
		}
		
		one.setCategory(new SupportCategory(form.getCategoryNo()));
		one.setTitle(form.getTitle());
		one.setContent(form.getContent());
		
		oneDao.insertOne(one);
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










