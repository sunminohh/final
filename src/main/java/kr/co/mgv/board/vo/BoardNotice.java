package kr.co.mgv.board.vo;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardNotice {

	private int no;
	private String boardType;
	private String boardName;
	private int boardNo;
	private String fromId;
	private String toId;
	private String code;
	private Date createDate;
	private String checked;
	@Builder
	public BoardNotice(int no, String boardType, String boardName, int boardNo, String fromId, String toId, String code, Date createDate,
			String checked) {
		super();
		this.no = no;
		this.boardType = boardType;
		this.boardName = boardName;
		this.boardNo = boardNo;
		this.fromId = fromId;
		this.toId = toId;
		this.code = code;
		this.createDate = createDate;
		this.checked = checked;
	}
}
