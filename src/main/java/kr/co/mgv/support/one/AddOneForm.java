package kr.co.mgv.support.one;

import java.util.Date;

import kr.co.mgv.support.SupportCategory;
import kr.co.mgv.theater.Theater;
import kr.co.mgv.theater.location.Location;
import kr.co.mgv.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddOneForm {

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
	private SupportCategory category;
	
}
