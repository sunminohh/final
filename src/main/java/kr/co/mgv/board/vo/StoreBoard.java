package kr.co.mgv.board.vo;

import java.util.Date;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreBoard {

	private int no;
	private String name;
	private String content;
	private Date createDate;
	private Date updateDate;
	private int readCount;
	private int commentCount;
	private String deleted;
	private String report;
	private User user;
	private BoardProduct product;
	private BoardCategory category;
	private String fileName;
	private int reportCount;
	private int likeCount;
	
	@Builder
	public StoreBoard(int no, String name, String content, Date createDate, Date updateDate, int readCount,
			int commentCount, String deleted, String report, User user, BoardProduct product, BoardCategory category,
			String fileName, int reportCount, int likeCount) {
		super();
		this.no = no;
		this.name = name;
		this.content = content;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.readCount = readCount;
		this.commentCount = commentCount;
		this.deleted = deleted;
		this.report = report;
		this.user = user;
		this.product = product;
		this.category = category;
		this.fileName = fileName;
		this.reportCount = reportCount;
		this.likeCount = likeCount;
	}
	
	
}
