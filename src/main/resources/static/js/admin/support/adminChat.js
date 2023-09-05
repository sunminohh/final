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
				 		<td style="color: white;">
				 		<img src="/images/board/sample.png" width="40" height="40" alt="프로필사진" style="border-radius: 50%;">
				 		<span style=" margin-left: 10px;">${userId}</span>
				 		</td>
				 	</tr>
				 `
				 $tbody.append(row);
			 });
			 
			 $("#table-waitings").text(waitings.userId);
		 } else if (data.cmd == "start") {
			roomId = data.roomId;
			
			appendMessagTag("left", userId, "["+userId+"]님과 연결되었습니다.");
			
		 } else if (data.cmd == "stop") {
			let userId = data.userId;
			appendMessagTag("left", userId, "["+userId+"]님과 상담이 종료되었습니다.");
			
			roomId = null;
			userId = null;
		 } else if (data.cmd == "msg") {
			roomId = data.roomId;
			let userId = data.userId;
			let text = data.text 
			
			if (userId == "admin") {
				appendMessagTag("right", "관리자", text);
			} else {
				appendMessagTag("left", userId, text);
			}
		 } else if (data.cmd == "exit") {
			 let userId = data.userId;
			 appendMessagTag("left", userId, "["+userId+"]님이 상담톡에서 나갔습니다.");
		 }
	 };
	 
    $("#table-waitings tbody").on("click", "span", function() {
		
		if (!userId) { // userId가 설정되지 않았을 때만 호출
	        userId = $(this).text();
			
			let message = {
				cmd:"start",
				userId: userId
			}
			console.log(message)
			ws.send(JSON.stringify(message));
	
			if (!$(this).next("a").length) { // 종료 버튼이 없을 때만 추가
	            let button = '<a class="float-end text-decoration-none text-danger" data-user-id="${userId}">종료</a>';
	            $(this).after(button);
	        }
        
        }
	})
	
	$("#table-waitings tbody").on("click", "a", function(event){
		event.preventDefault();
	
		let message = {
			cmd:"stop",
			userId: userId
		}
		ws.send(JSON.stringify(message));
		
		$(this).closest("td").remove();
		
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
			 <li class="${className}">
                <div class="sender">
                    <span>${userName}</span>
                </div>
                <div class="message">
                    <span>${text}</span>
                </div>
            </li>
		`
		$('div.chat:not(.format) ul').append(tag);
        
		// 스크롤바 아래 고정
        $('div.chat').scrollTop($('div.chat').prop('scrollHeight'));
	}
	
	// 메세지 입력박스 내용 지우기
	function clearTextarea() {
		$('div.input-div textarea').val('');
	}
	
	function send(message) {
		ws.send(JSON.stringify(message));
	}
	
});









