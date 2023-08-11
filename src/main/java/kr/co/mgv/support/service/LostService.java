package kr.co.mgv.support.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mgv.support.dao.LostDao;
import kr.co.mgv.support.form.AddLostForm;
import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostService {

	private final LostDao lostDao;
	
	public void insertLost(AddLostForm form, User user) {
		Lost lost = new Lost();
		
		if (user != null) {
			lost.setUser(user);
		} else {
			lost.setGuestName(form.getGuestName());
			lost.setGuestEmail(form.getGuestEmail());
			lost.setGuestPassword(form.getGuestPassword());
		}
		
		lost.setLocation(new Location(form.getLocationNo()));
		lost.setTheater(new Theater(form.getTheaterNo()));
		lost.setTitle(form.getTitle());
		lost.setContent(form.getContent());
		
		lostDao.insertLost(lost);
	}
	
	public List<Location> getLocations() {
		
		return lostDao.getLocations();
	}
	
	public List<Theater> getTheatesrByLocationNo(int locationNo) {
		
		return lostDao.getTheatersByLocationNo(locationNo);
	}
}

















