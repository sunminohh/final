/**
 * 좋아요 버튼 관련 코드
 */
$(function() {
	let likeButtonClicked = false;
	$("#like-button").click(function(event) {
		event.preventDefault();
		if (!likeButtonClicked) {
			$(this).removeClass("btn-outline-secondary").addClass("btn-secondary");
			likeButtonClicked = true;
		} else {
			$(this).removeClass("btn-secondary").addClass("btn-outline-secondary");
			likeButtonClicked = false;
		}
	})

$(function() {
	
	$("#comment-box").click(function(event) {
		event.preventDefault();		
		

		let content = `
	   				<div id="profile" >
						<div id="comment-imgbox" >
							<a href="사용자상세정보">
								<img id="comment-profileimg" th:src="@{/images/board/sample.png}" alt="프로필사진">
							</a>
						</div>
	
					</div>
					<div class="commentUserInfo ps-5" >
							<p>댓글쓴놈 아이디</p> 
		   					<p>내용이요</p>
		   					<div class="d-flex justify-content-start">
		   						<p class="float-start me-2" style="font-size: 12px; color: gray">1998/06/24 07:00:00</p>
		   						<a href="" class="float-satrt" style="text-decoration:none; font-size: 12px; color: gray">답글쓰기</a>
		   					</div>
						
					</div>
		   			<div >
		   				
		   				<a  th:href="@{/deleteComment(no=1, cno=1)}" 
		   					class="btn btn-link text-danger text-decoration-none float-end"><i class="bi bi-trash"></i></a>
	
	   				</div>   			
		`
	
			$("#reply-comment-box").append(content)
	})
})


	
})