package kr.co.mgv.board.sboard;

import java.util.Date;

import kr.co.mgv.user.vo.User;
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
	private String filename;
	private int reportCount;
	private int likeCount;
}
