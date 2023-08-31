$(function(){
    
    let userId;
    // 웹소켓 객체를 생성하고, 웹소켓 연결요청을 서버로 보낸다.
    let ws  = new SockJS("/chat");
    let roomId = null;
    
    // 웹소켓이 연결이 완료되면 함수가 실행된다.
    ws.onopen = function() {
    	let message = {
			cmd:"ready",
			userId: "admin"
		}
		console.log(message)
		ws.send(JSON.stringify(message));
 	};
 	
 	// 서버가 보낸 메세지가 수신되면 함수가 실행된다.
 	ws.onmessage = function(message) {
		 let data = JSON.parse(message.data);
		 console.log(data);
		 
		 if (data.cmd == "wait") {
			 let waitings = data.waitings;
			 console.log("대기자 목록", waitings)
			 
			 $tbody = $("#table-waitings tbody").empty();
			 
			 waitings.forEach(function(userId) {
				 let row = `
				 	<tr>
				 		<td>${userId}</td>
				 	</tr>
				 `
				 $tbody.append(row);
			 });
			 
			 $("#table-waitings").text(waitings.userId);
		 } else if (data.cmd == "start") {
			roomId = data.roomId;
			userId = data.userId;
			
			appendMessagTag("left", userId, "["+userId+"]님과 연결되었습니다.");
			
		 } else if (data.cmd == "stop") {
			
		 } else if (data.cmd == "msg") {
			roomId = data.roomId;
			let userId = data.userId;
			let text = data.text 
			
			if (userId == "admin") {
				appendMessagTag("right", "관리자", text);
			} else {
				appendMessagTag("left", userId, text);
			}
		 }
	 };
	 
    $("#table-waitings tbody").on("click", "td", function() {
		
		let message = {
			cmd:"start",
			userId: $(this).text()
		}
		console.log(message)
		ws.send(JSON.stringify(message));
	})
	
	$(document).on('keydown', 'div.input-div textarea', function(e){
		if(e.keyCode == 13 && !e.shiftKey) {
			e.preventDefault();
			
			let message =  {
				cmd: 'msg',
				roomId: roomId,
				userId: "admin",
				receiverId: userId,
				text: $(this).val()
			}
			// 메시지 전송
			send(message);
			// 입력창 clear
			clearTextarea();
		}
	});
	
	function appendMessagTag(className, userName, text) {
		let tag = `
			 <li class="${className}>
                <div class="sender">
                    <span>${userName}</span>
                </div>
                <div class="message">
                    <span>${text}</span>
                </div>
            </li>
		`
		$('div.chat:not(.format) ul').append(tag);
	}
	
	// 메세지 입력박스 내용 지우기
	function clearTextarea() {
		$('div.input-div textarea').val('');
	}
	
	function send(message) {
		ws.send(JSON.stringify(message));
	}
	
});









