package kr.co.mgv.board.vo;

import java.util.Date;

import kr.co.mgv.schedule.vo.Schedule;
import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartyBoard {

	private int no;
	private String name;
	private String content;
	private String report;
	private int reportCount;
	private int readCount;
	private int commentCount;
	private int likeCount;
	private int headCount;
	private User user;
	private String deleted;
	private String complete;
	private Date createDate;
	private Date updateDate;
	private String Gender;
	private PartyBoardSchedule schedule;
	
	@Builder
	public PartyBoard(int no, String name, String content, String report, int reportCount, int readCount,
			int commentCount, int likeCount, int headCount, User user, String deleted, String complete, Date createDate,
			Date updateDate, String gender, PartyBoardSchedule schedule) {
		super();
		this.no = no;
		this.name = name;
		this.content = content;
		this.report = report;
		this.reportCount = reportCount;
		this.readCount = readCount;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.headCount = headCount;
		this.user = user;
		this.deleted = deleted;
		this.complete = complete;
		this.createDate = createDate;
		this.updateDate = updateDate;
		Gender = gender;
		this.schedule = schedule;
	}
}
