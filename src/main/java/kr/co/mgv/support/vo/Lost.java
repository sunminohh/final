package kr.co.mgv.support.vo;

import java.util.Date;

import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Lost {

	private int no;
	private String title;
	private String content;
	private String answered;
	private String deleted;
	private String guestName;
	private String guestEmail; 
	private int guestPassword;
	private String fileName;
	private Date updateDate;
	private Date createDate;
	private User user;
	private Location location;
	private Theater theater;
	
	
	
}
