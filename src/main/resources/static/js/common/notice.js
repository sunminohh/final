$(function() {
    let noticeWS = new SockJS("/notice");
    noticeWS.onmessage = function(message) {
        let text = message.data;
        // 알람 표시
    };
});