$(function() {
	
	
	
})
	let noticeWS = new SockJs("/notice");
	noticeWS.onmaessage = function(message) {
		let text = message.data
		// 알람표시
	}