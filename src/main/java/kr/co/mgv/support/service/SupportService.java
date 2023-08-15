package kr.co.mgv.support.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mgv.support.dao.SupportDao;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupportService {

	private final SupportDao supportDao;
	
	public List<Location> getLocations() {
		return supportDao.getLocations();
	}
	
	public List<Theater> getTheatesrByLocationNo(int locationNo) {
		return supportDao.getTheatersByLocationNo(locationNo);
	}
}
