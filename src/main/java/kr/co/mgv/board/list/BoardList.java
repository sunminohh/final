package kr.co.mgv.board.list;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardList {

	private int no;
	private Date createDate;
	private String name;
	private int readCount;
	private String type;
	private String id;
	private String content;
	private int reportNo;
	@Builder
	public BoardList(int no, Date createDate, String name, int readCount, String type, String id, 
			String content, int reportNo) {
		super();
		this.no = no;
		this.createDate = createDate;
		this.name = name;
		this.readCount = readCount;
		this.type = type;
		this.id = id;
		this.content = content;
		this.reportNo = reportNo;
	}
}
