package kr.co.mgv.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.form.AddBoardNoticeForm;
import kr.co.mgv.board.vo.BoardNotice;

@Mapper
public interface BoardNoticeDao {

	void insertNotice(AddBoardNoticeForm form);
	List<BoardNotice> getNoticesByToId(String toId);
	int getTotalNotice(String toId);
	int getNoticeNo();
}
