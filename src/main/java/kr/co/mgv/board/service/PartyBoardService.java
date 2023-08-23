package kr.co.mgv.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.form.AddPboardForm;
import kr.co.mgv.board.mapper.PartyBoardDao;
import kr.co.mgv.board.vo.PartyBoard;
import kr.co.mgv.board.vo.PartyBoardSchedule;
import kr.co.mgv.schedule.vo.Schedule;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyBoardService {

	private final PartyBoardDao partyBoardDao;
	
	// 등록폼 관련
	public List<PartyBoardSchedule> getScheduleList(Map<String, Object> param) {
		return partyBoardDao.getSceduleByDateAndTNoAndMNo(param);
	} 
	
	public PartyBoardSchedule getSceduleById(int id) {
		return partyBoardDao.getSceduleById(id);
	}
	
	// CRUD
	public void insertPBoard (AddPboardForm form, User user) {
		
		PartyBoardSchedule schedule = PartyBoardSchedule.builder()
									  .id(form.getScheduleId()).build();
		PartyBoard board = PartyBoard.builder()
						   .user(user)
						   .name(form.getName())
						   .content(form.getContent())
						   .headCount(form.getHeadCount())
						   .gender(form.getGender())
						   .schedule(schedule)
						   .build();
		partyBoardDao.insertPboard(board);
	}
	
}
