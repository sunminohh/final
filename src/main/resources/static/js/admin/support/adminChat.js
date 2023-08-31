const Chat = (function(){
    const myName = "blue";
 
    // init 함수
    function init() {
        // enter 키 이벤트
        $(document).on('keydown', 'div.input-div textarea', function(e){
            if(e.keyCode == 13 && !e.shiftKey) {
                e.preventDefault();
                const message = $(this).val();
 
                // 메시지 전송
                sendMessage(message);
                // 입력창 clear
                clearTextarea();
            }
        });
    }
 
    // 메세지 태그 생성
    function createMessageTag(LR_className, senderName, message) {
        // 형식 가져오기
        let chatLi = $('div.chat.format ul li').clone();
 
        // 값 채우기
        chatLi.addClass(LR_className);
        chatLi.find('.sender span').text(senderName);
        chatLi.find('.message span').text(message);
 
        return chatLi;
    }
 
    // 메세지 태그 append
    function appendMessageTag(LR_className, senderName, message) {
        const chatLi = createMessageTag(LR_className, senderName, message);
 
        $('div.chat:not(.format) ul').append(chatLi);
 
        // 스크롤바 아래 고정
        $('div.chat').scrollTop($('div.chat').prop('scrollHeight'));
    }
 
    // 메세지 전송
    function sendMessage(message) {
        // 서버에 전송하는 코드로 후에 대체
        const data = {
            "senderName"    : "Admin",
            "message"        : message
        };
 
        // 통신하는 기능이 없으므로 여기서 receive
        resive(data);
    }
 
    // 메세지 입력박스 내용 지우기
    function clearTextarea() {
        $('div.input-div textarea').val('');
    }
 
    // 메세지 수신
    function resive(data) {
        const LR = (data.senderName != myName)? "left" : "right";
        appendMessageTag("right", data.senderName, data.message);
    }
 
    return {
        'init': init
    };
})();
 
$(function(){
    Chat.init();
    
    let roomId;
    
    // 웹소켓 객체를 생성하고, 웹소켓 연결요청을 서버로 보낸다.
    let ws  = new SockJS("/chat");
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
			
		 } else if (data.cmd == "stop") {
			
		 } else if (data.cmd == "msg") {
			
		 }
	 };
	 
	 
    
});









