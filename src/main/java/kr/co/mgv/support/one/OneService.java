package kr.co.mgv.support.one;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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


}
