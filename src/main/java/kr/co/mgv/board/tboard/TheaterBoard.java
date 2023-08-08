package kr.co.mgv.board.tboard;

import java.util.Date;

import kr.co.mgv.user.vo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TheaterBoard {
	
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
	private BoardTheater theater;
	private BoardLocation location;
	private String fileName;
	private int reportCount;
	private int likeCount;
}
