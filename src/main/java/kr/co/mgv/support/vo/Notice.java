package kr.co.mgv.support.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import groovy.transform.builder.Builder;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Notice {

	/*
	NOTICE_NO,
	 NOTICE_TITLE,
	  NOTICE_CONTENT, 
	  NOTICE_DELETED, 
	  NOTICE_UPDATE_DATE, 
	  NOTICE_CREATE_DATE,
	   USER_ID,
	    LOCATION_NO,
	    THEATER_NO, 
	    CATEGORY_NO
	*/
	private int no;
	private String title;
	private String content;
	private String deleted;
	@JsonFormat(pattern = "yyyy.MM.dd")
	private Date updateDate;
	@JsonFormat(pattern = "yyyy.MM.dd")
	private Date createDate;
	private User user;
	private Location location;
	private Theater theater;
	private SupportCategory category;
	
	@Builder
	public Notice(int no, String title, String content, String deleted, Date updateDate, Date createDate, User user,
			Location location, Theater theater, SupportCategory category) {
		super();
		this.no = no;
		this.title = title;
		this.content = content;
		this.deleted = deleted;
		this.updateDate = updateDate;
		this.createDate = createDate;
		this.user = user;
		this.location = location;
		this.theater = theater;
		this.category = category;
	}
	
}











