package kr.co.mgv.event.vo;

import java.util.Date;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Event {

	private int no;
	private String title;
	private String mainImage;
	private String detailImage;
	private String deleted;
	private Date startDate;
	private Date endDate;
	private Date updateDate;
	private Date createDate;
	private EventCategory category;
	private User user;
	
	@Builder
	public Event(int no, String title, String mainImage, String detailImage, String deleted, Date startDate,
			Date endDate, Date updateDate, Date createDate, EventCategory category, User user) {
		super();
		this.no = no;
		this.title = title;
		this.mainImage = mainImage;
		this.detailImage = detailImage;
		this.deleted = deleted;
		this.startDate = startDate;
		this.endDate = endDate;
		this.updateDate = updateDate;
		this.createDate = createDate;
		this.category = category;
		this.user = user;
	}
	
	
	
	
}






