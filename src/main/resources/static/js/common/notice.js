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
		let totalNotice = parseInt($(".totalCnt").text())+1;
		
		
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
        				 <input type="hidden" name="noticeNo" value="${boardNo}">
        			     <hr class="mt-4"/>`;
        const noticeIcon = `알림<span class="red-dot"></span>`
        			     
        $(".no-list").prepend(content);
        $(".totalCnt").empty().append(totalNotice);
        $(".notice").empty().append(noticeIcon);
    };
    
    $(".notice").click(function() {
		$(".notice").empty().append('알림');
		$.ajax({
		    url: '/notice/getNotices',
		    method: 'GET',
		    success: function(result) {
				let notices = result.notices;
				let totalNotice = result.totalNotice;
				let content ="";
				let codeContent ="";
				let href = "";
				
		        notices.forEach(function(notice){
					let type = notice.boardType;
					let code = notice.code;
					if(code === 'comment'){
						codeContent = '님이 댓글을 달았습니다.'
					}
					if(code === 'reComment'){
						codeContent = '님이 대댓글을 달았습니다.'
					}
					if(code === 'like'){
						codeContent = '님이 게시글을 좋아합니다.'
					}
					if (type === '영화') {
			            href = `http://localhost/board/movie/detail?no=${notice.boardNo}`;
			        }
			        if (type === '극장') {
			            href = `http://localhost/board/theater/detail?no=${notice.boardNo}`;
			        }
			        if (type === '스토어') {
			            href = `http://localhost/board/store/detail?no=${notice.boardNo}`;
			        }
			        if (type === '파티') {
			            href = `http://localhost/board/party/detail?no=${notice.boardNo}`;
			        }
				
					content += `
						<a href="${href}" class="float-start fw-semibold" style="color:#01738b;">[${type}]게시판 [${notice.boardName}...]</a><br/>
        				<p class="float-start ms-1">${notice.fromId}님이 ${codeContent}</p>
        			    <input type="hidden" name="noticeNo" value="${notice.no}">
        			    <hr class="mt-4"/>
					`
				})
				console.log(notices);
				
				$(".totalCnt").empty().append(totalNotice);
				$(".no-list").empty().append(content);
		    },
		    error: function(xhr, status, error) {
		    }
		});
	})

});
