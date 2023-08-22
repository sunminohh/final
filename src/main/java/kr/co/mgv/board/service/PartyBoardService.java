package kr.co.mgv.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.mapper.PartyBoardDao;
import kr.co.mgv.board.vo.PartyBoardSchedule;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyBoardService {

	private final PartyBoardDao partyBoardDao;
	
	public List<PartyBoardSchedule> getScheduleList(Map<String, Object> param) {
		return partyBoardDao.getSceduleByDateAndTNoAndMNo(param);
	} 
	
	public PartyBoardSchedule getSceduleById(int id) {
		return partyBoardDao.getSceduleById(id);
	}
}
