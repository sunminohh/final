$(function() {
    let noticeWS = new SockJS("/notice");
    
    noticeWS.onopen = function() {
        console.log("웹소켓 연결이 완료되었습니다.");
    };

    noticeWS.onmessage = function(message) {
        console.log("응답메세지-----------", message.data);
        let text = message.data;
        let type = text.match(/\[.*?\]/)[0];
        let boardNoMatch = text.match(/\[(.*?)\]/);
        let boardNo = boardNoMatch && boardNoMatch.length > 1 ? boardNoMatch[1] : null;

        console.log(type);
        let href;
        if (type === '[영화]' && boardNo) {
            href = `http://localhost/board/movie?no=${boardNo}`;
        }
        let content = `<a href="${href}">${text}</a>`;
        $(".no-list").prepend(content);
    };
});
