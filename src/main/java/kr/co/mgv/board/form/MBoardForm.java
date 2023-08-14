package kr.co.mgv.board.form;

import java.util.Date;

import kr.co.mgv.movie.vo.Movie;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MBoardForm {
	
	private int no;
	private String name;
	private String content;
	// 게시물 '수정'시에만 Y를 전달
	// 신고관련, 조회수, 댓글수 등의 update에는 null
	private Date updateDate;
	private int readCount;
	private int commentCount;
	private String deleted;
	private String report;
	private Movie movie;
	private String fileName;
	private int reportCount;
	private int likeCount;
	
	@Builder
	public MBoardForm(int no, String name, String content, Date updateDate, int readCount, int commentCount,
			String deleted, String report, Movie movie, String fileName, int reportCount, int likeCount) {
		super();
		this.no = no;
		this.name = name;
		this.content = content;
		this.updateDate = updateDate;
		this.readCount = readCount;
		this.commentCount = commentCount;
		this.deleted = deleted;
		this.report = report;
		this.movie = movie;
		this.fileName = fileName;
		this.reportCount = reportCount;
		this.likeCount = likeCount;
	}
	
	
}
