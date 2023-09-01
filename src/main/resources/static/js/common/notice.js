$(function() {
    let noticeWS = new SockJS("/notice");
    
    noticeWS.onopen = function() {
        console.log("웹소켓 연결이 완료되었습니다.");
    };

    noticeWS.onmessage = function(message) {
        console.log("응답메세지-----------", message.data);
        const text = message.data;
        const type = text.match(/\[.*?\]/)[0];
        const index = text.lastIndexOf(".");
        const boardNo = text.substring(index + 1);
        
        const startIndex = text.indexOf("[");
		const endIndex = text.indexOf(".]") + 2;
		const titleResult = text.substring(startIndex, endIndex);

        const contentStartIndex = text.indexOf("에")+1;
		const contentEndIndex = text.lastIndexOf(".")+1;
		const contentResult = text.substring(contentStartIndex, contentEndIndex);

        console.log(type);
        let href;
        if (type === '[영화]' && boardNo) {
            href = `http://localhost/board/movie/detail?no=${boardNo}`;
        }
        if (type === '[극장]' && boardNo) {
            href = `http://localhost/board/theater/detail?no=${boardNo}`;
        }
        if (type === '[스토어]' && boardNo) {
            href = `http://localhost/board/store/detail?no=${boardNo}`;
        }
        if (type === '[파티]' && boardNo) {
            href = `http://localhost/board/party/detail?no=${boardNo}`;
        }
        const content = `<a href="${href}" class="float-start fw-semibold" style="color:#01738b;">${titleResult}</a><br/>
        				 <p class="float-start ms-1">${contentResult}</p>
        			     <hr class="mt-4"/>`;
        $(".no-list").prepend(content);
    };
});
