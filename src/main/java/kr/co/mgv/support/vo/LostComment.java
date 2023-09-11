package kr.co.mgv.support.vo;

import java.util.Date;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LostComment {

	private int no;
	private String content;
	private String deleted;
	private Date updateDate;
	private Date creatDate;
	private Lost lost;
	private User user;
	
	@Builder
	public LostComment(int no, String content, String deleted, Date updateDate, Date creatDate, Lost lost, User user) {
		super();
		this.no = no;
		this.content = content;
		this.deleted = deleted;
		this.updateDate = updateDate;
		this.creatDate = creatDate;
		this.lost = lost;
		this.user = user;
	}
	
	
	public String getHtmlContent() {
		if (content == null) {
			return null;
		}
		return content.replace(System.lineSeparator(), "<br />");
	}
}
