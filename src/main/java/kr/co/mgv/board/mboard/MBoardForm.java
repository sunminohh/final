package kr.co.mgv.board.mboard;

import java.util.Date;

import kr.co.mgv.movie.vo.Movie;
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
	private String updateDate;
	private int readCount;
	private int commentCount;
	private String deleted;
	private String report;
//	private Movie movie;
	private String fileName;
	private int reportCount;
	private int likeCount;
}
