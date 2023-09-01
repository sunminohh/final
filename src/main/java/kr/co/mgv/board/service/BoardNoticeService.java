package kr.co.mgv.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.list.BoardNoticeList;
import kr.co.mgv.board.mapper.BoardNoticeDao;
import kr.co.mgv.board.vo.BoardNotice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardNoticeService {

	private final BoardNoticeDao boardNoticeDao;
	
	public BoardNoticeList getNoticeByToId(String toId){
		List<BoardNotice> notices = boardNoticeDao.getNoticesByToId(toId);
		int totalNotice = boardNoticeDao.getTotalNotice(toId);
		BoardNoticeList result = BoardNoticeList.builder().notices(notices).totalNotice(totalNotice).build();
		return result;
	}
	
}
