package kr.co.mgv.board.mboard;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class MovieBoard {

	private int no;
	private String name;
	private String content;
	private Date createDate;
	private Date updateDate;
	private int readCount;
	private int commentCount;
	private String deleted;
	private String report;

	
}