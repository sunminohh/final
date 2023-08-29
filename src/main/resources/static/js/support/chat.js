$(function() {
  const messagesContainer = document.getElementById('messages');
  const userInput = document.getElementById('userInput');

  function sendMessage() {
    const message = userInput.value.trim();
    if (message !== '') {
      // 유저 메시지 표시
      displayUserMessage(message);

      // 채팅 메시지를 서버로 보내는 부분은 여기에 추가하세요.
      // 서버로 메시지를 보내는 코드 또는 WebSocket을 사용하여 실시간으로 전달할 수 있습니다.

      userInput.value = '';
    }
  }

  
  
  function displayUserMessage(message) {
    $("#messages").append(`<div class="message user-message">${message}</div>`)
    
    if ($("#messages").height() > 242.5) {
		$(window).scrollTop($("#messages").height() - 242.5)
	};
  }

  function displayBotMessage(message) {
    const messageElement = document.createElement('div');
    messageElement.className = 'message bot-message';
    
    // "안녕하세요. MGV입니다."와 "무엇을 도와드릴까요?" 사이에 줄바꿈 추가
    messageElement.innerHTML = message + '<br>무엇을 도와드릴까요?';
    
    messagesContainer.appendChild(messageElement);
  }

  // 전송 버튼 클릭 이벤트에 sendMessage 함수를 연결합니다.
  document.querySelector('#sendButton').addEventListener('click', sendMessage); // 버튼을 id로 선택합니다.

  // Enter 키를 눌러도 메시지를 전송할 수 있도록 이벤트를 추가합니다.
  userInput.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
      sendMessage();
    }
  });

  // 상담톡이 열리면 초기 메시지를 표시합니다.
  displayBotMessage('안녕하세요. MGV입니다.');
  
});
