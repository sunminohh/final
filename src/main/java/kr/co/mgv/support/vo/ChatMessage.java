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
public class ChatMessage {

	private int no;
	private ChatRoom chatRoom;
	private User user;
	private String content;
	private Date sendTime;
	
	@Builder
	public ChatMessage(int no, ChatRoom chatRoom, User user, String content, Date sendTime) {
		super();
		this.no = no;
		this.chatRoom = chatRoom;
		this.user = user;
		this.content = content;
		this.sendTime = sendTime;
	}
	
	
	
}
