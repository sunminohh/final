package kr.co.mgv.support.vo;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

	/*
	 * 채팅 명령
	 * req		: 채팅요청	
	 * ready	: 채팅준비
	 * start 	: 채팅시작
	 * msg		: 메세지
	 * cancel	: 채팅취소
	 * stop		: 채팅종료
	 */
	
	private String cmd;
	private int no;
	private String roomId;
	private String userId;
	private String receiverId;
	private String text;
	private Date sendTime;
	List<String> waitings;
	private int position;
	
}
