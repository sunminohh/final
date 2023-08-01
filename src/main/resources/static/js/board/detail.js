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
		
		$("#a-reply").click(function(event) {
			event.preventDefault();		
	
			let content = `
		   	<div class="col-11 ms-5 CommentWriter">
				<div class="comment-inbox border p-2 rounded"> 
	
					<em class="comment_inbox_name">로그인된 아이디</em>
					
					<form class="" method="post" action="insertComment.jsp">
						<input type="hidden" name="no" value= />
		 				<div class="row">
							<div >
								<textarea rows="2" class="comment_inbox_text" name="content" style="border: none; overflow: hidden; overflow-wrap: break-word; " 
								placeholder="댓글을 남겨보세요"></textarea>
							</div>
							<div class="register_box">
								<button class="btn btn-outline-white btn-sm float-end" style="border: none">등록</button>
							</div>
						</div>
					</form>   	
				</div>
	   		</div>  			
			`;

			$("#reply-comment-box").append(content)
			$("#a-reply").attr('id', 'reply').text('취소')
			
		})
	})
	
	

	$(function() {
		
		$("#reply").click(function(event) {
			event.preventDefault();	
			
			$("#reply-comment-box").html(" ")
			$("#reply").attr('id', 'a-reply').text('답글쓰기')
		
		})
		
	})
	
})