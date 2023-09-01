package kr.co.mgv.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.form.AddBoardNoticeForm;

@Mapper
public interface BoardNoticeDao {

	void insertNotice(AddBoardNoticeForm form);
}
