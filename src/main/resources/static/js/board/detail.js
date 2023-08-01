
		
/**
 * 좋아요 버튼 관련 코드
 */
	$(function() {
		let likeButtonClicked = false;
		$("#like-button").click(function(event) {
			event.preventDefault();
			if (!likeButtonClicked) {
				$(this).removeClass("btn-outline-secondary").addClass("btn-secondary")
					   .text("좋아요♥");
				likeButtonClicked = true;
			} else {
				$(this).removeClass("btn-secondary").addClass("btn-outline-secondary")
						.text("좋아요♡");
				likeButtonClicked = false;
			}
		});

/*
	id="a-reply"인 a태그 '답글달기'를 클릭시 id="reply-comment-box"인 div 안에 html 코드(let content)가 append 되고,
	해당 a태그의 text는 '취소'로, id는 reply로 변경된다.
	-> 변경된 text인 '취소'를 눌렀을때 id="reply-comment-box"인 div 안에 content가 추가되지 않는다
*/

		
		$(".commentUserInfo").on("click", "#a-reply", function(event) {
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
	
/*
	id="reply"인 a태그 '취소'를 클릭시 id="reply-comment-box"인 div 안에(" ")가 html 되고,
	해당 a태그의 text는 '취소'로, id는 reply로 변경된다.
*/	

		
		$(".commentUserInfo").on("click", "#reply", function(event) {
			event.preventDefault();	
			
			$("#reply-comment-box").empty()
			$("#reply").attr('id', 'a-reply').text('답글쓰기')
		
		})
		
		

			$(".re-commentUserInfo").on("click", "#a-re-reply", function(event) {
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

			$("#re-reply-comment-box").append(content)
			$("#a-re-reply").attr('id', 're-reply').text('취소')
			
		})
		
		$(".re-commentUserInfo").on("click", "#re-reply", function(event) {
			event.preventDefault();	
			
			$("#re-reply-comment-box").empty()
			$("#re-reply").attr('id', 'a-re-reply').text('답글쓰기')
		
		})

	
})



		