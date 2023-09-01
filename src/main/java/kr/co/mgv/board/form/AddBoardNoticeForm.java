package kr.co.mgv.board.form;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddBoardNoticeForm {

	private String boardType;
	private String boardName;
	private int boardNo;
	private String fromId;
	private String toId;
	private String code;
	
	@Builder
	public AddBoardNoticeForm(String boardType, String boardName, int boardNo, String fromId, String toId, String code) {
		super();
		this.boardType = boardType;
		this.boardName = boardName;
		this.boardNo = boardNo;
		this.fromId = fromId;
		this.toId = toId;
		this.code = code;
	}
}

