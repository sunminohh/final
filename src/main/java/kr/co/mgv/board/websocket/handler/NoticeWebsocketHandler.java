package kr.co.mgv.board.websocket.handler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoticeWebsocketHandler extends TextWebSocketHandler {

	private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<String, WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("로그인 아이디 -----------------" + getUserId(session));
		String loginId = getUserId(session);
		System.out.println("웹소켓 연결요청이 접수됨.........." + loginId);
		sessions.put(loginId, session);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("웹소켓 연결이 종료됨..........");
		String loginId = getUserId(session);
		sessions.remove(loginId);
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("웹소켓 데이터 전송 중 오류가 발생함.............");
		String loginId = getUserId(session);
		sessions.remove(loginId);
	}
	
	public void sendMessage(String userId, String text) throws IOException {
		System.out.println("["+userId+"] ["+text+"]");
		TextMessage message = new TextMessage(text);
		
		WebSocketSession session = sessions.get(userId);
		System.out.println("세션["+ session+"]");
		if(session != null) {
			session.sendMessage(message);
		}
	}
	
	private String getUserId(WebSocketSession session) {
		return 
				((UsernamePasswordAuthenticationToken)((SecurityContext)session.getAttributes().get("SPRING_SECURITY_CONTEXT")).getAuthentication()).getName();
	}
	
}
