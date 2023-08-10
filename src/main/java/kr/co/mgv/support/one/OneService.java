package kr.co.mgv.support.one;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import kr.co.mgv.support.SupportCategory;
import kr.co.mgv.support.SupportLocation;
import kr.co.mgv.support.SupportTheater;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OneService {
	
	private final OneDao oneDao;

	public void createOne(AddOneForm form) {
		One one = new One();
		BeanUtils.copyProperties(form, one);
		
		oneDao.insertOne(one);
	}

	public List<SupportCategory> getCategoriesByType(String categoryType) {
		
		return oneDao.getCategories(categoryType);
	}
	
	public List<SupportLocation> getLocations() {
		
		return oneDao.getLocations();
	}
	
	public List<SupportTheater> getTheatesrByLocationNo(int locationNo) {
		
		return oneDao.getTheatersByLocationNo(locationNo);
	}

}










