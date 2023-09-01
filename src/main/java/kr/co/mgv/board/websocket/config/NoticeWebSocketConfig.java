package kr.co.mgv.board.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import kr.co.mgv.board.websocket.handler.NoticeWebsocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class NoticeWebSocketConfig implements WebSocketConfigurer{

	private final NoticeWebsocketHandler noticeWebsocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(noticeWebsocketHandler, "/notice").withSockJS().setInterceptors(new HttpSessionHandshakeInterceptor());
	}
}
