
		
/**
 * 좋아요 버튼 관련 코드
 */
$(function() {
		let like = document.querySelector("input[name=likeCount]").value;
		
		$("#like-button").click(function(event) {
		    event.preventDefault();
		    let type= $(this).attr("data-button-type");
		    if (type == 'plus') {
			    like++;				
			} else if (type == 'minus') {
				like--;
			}
		    $("input[name=likeCount]").val(like); // 좋아요 수 업데이트
		    $("#guest-like-count").text(like); // 좋아요 수 업데이트
		    
		    $(this).next().text(like);
		
		    // AJAX 요청
		    $.ajax({
		        url: '/board/movie/changelike',
		        method: "POST",
		        data: $("#like-btn-form").serialize(),
		        success: function(response) {
		            // 성공 시 업데이트된 내용을 특정 부분에 적용
		            if (type == 'plus') {
						$("#like-button").text('♥').attr('data-button-type', 'minus')		
					} else if (type == 'minus') {
						$("#like-button").text('♡').attr('data-button-type', 'plus')		
					}
		        }
		    });
		});

/*
	신청버튼 관련 코드
*/	
		let joinButtonClicked = false;
		$("#join-button").click(function(event) {
			event.preventDefault();
			if (!joinButtonClicked) {
				$(this).removeClass("btn-outline-secondary").addClass("btn-secondary")
					   .text("취소하기");
				joinButtonClicked = true;
			} else {
				$(this).removeClass("btn-secondary").addClass("btn-outline-secondary")
						.text("신청하기");
				joinButtonClicked = false;
			}
		});





// 댓글 관련 코드

       	let no = document.querySelector("input[name=no]").value;
		let loginId = $("#login-id").attr("value");
		let commentNo = $(this).attr("data-cancel-comment");

	    // 답글 작성 버튼을 클릭하면
	    $(".commentUserInfo").on("click", "[data-comment-no]", function (event) {
	        event.preventDefault();

       		let commentNo = $(this).attr("data-comment-no");

	        // 이미 답글 작성 폼이 열려있는지 확인하고, 열려있으면 닫기
	        if ($("#reply-form-" + commentNo).length > 0) {
	            $("#reply-form-" + commentNo).remove();
	            $("#btn-a-re-reply-" + commentNo).attr('id', 'btn-a-reply-' + commentNo).text('답글쓰기');
	            return;
	        }

	        // 답글 작성 폼 HTML
	        let content = `
	            <div class="col-11 ms-5 CommentWriter" id="reply-form-${commentNo}">
	                <div class="comment-inbox border p-2 rounded">
	                    <em class="comment_inbox_name">${loginId}</em>
	                    <form id="re-form-comment" method="post" action="/board/movie/addReComment">
	                        <input type="hidden" name="no" value="${no}" />
	                        <input type="hidden" name="parentNo" value="${commentNo}" />
	                        <input type="hidden" name="greatNo" value="${commentNo}" />
							<input type="hidden" name="id" value=${loginId} />
	                        <div class="row">
	                            <div id="new-content-div">
	                                <textarea rows="2" class="comment_inbox_text" name="content" id="content"
	                                          style="border: none; overflow: hidden; overflow-wrap: break-word;"
	                                          placeholder="댓글을 남겨보세요"></textarea>
	                            </div>
	                            	<div class="re-register-box">
	
		                               <button class="btn btn-outline-white btn-sm float-end" id="btn-comment" style="border: none" type="button">등록</button>
		                            </div>
		                        </div>
		                    </form>
		                </div>
		            </div>`;
		
		        // 답글 작성 폼 추가
		        $("#reply-comment-box-" + commentNo).append(content);
		        // 답글 쓰기 버튼을 취소 버튼으로 변경
		        $("#btn-a-reply-" + commentNo).attr('id', 'btn-a-re-reply-' + commentNo).text('취소');
	    });
	
	    // 답글 작성 취소 버튼을 클릭하면 폼을 제거하고 버튼을 답글쓰기로 변경
	    $(".commentUserInfo").on("click", "[data-cancel-comment]", function (event) {
	        event.preventDefault();
	
	
	        // 답글 작성 폼을 제거하고 버튼을 답글쓰기로 변경
	        $("#reply-form-" + commentNo).remove();
	        $("#btn-a-re-reply-" + commentNo).attr('id', 'btn-a-reply-' + commentNo).text('답글쓰기');
	    });
		
		
		
		// 댓글 axax
		$(".register-box").on('click','#btn-comment' ,function() {
			
			let inputcontent = $("#content-box textarea").val();
           	let id = $("input[name=userId]").val;
    
		    if(inputcontent === "") { // 댓글 내용이 비어있을 때
		        Swal.fire({
		            icon: 'error',
		            text: '댓글 내용을 작성해주세요.',
		            onClose: () => {
		                $("#content-box textarea").focus(); // 입력 필드로 포커스 이동
		            }
		        });
		    } else { // 댓글 내용이 비어있지 않을 때
		        // AJAX 요청을 보내고 새로운 댓글 목록을 받아옴
			    $.ajax({
			        url: '/board/movie/addComment',
			        method: "POST",
			        data: $("#form-comment").serialize(),
			        success: function(comment) {
			            $("#content-box textarea").val('');
			           	
						
			            // 성공 시 새로운 댓글 목록을 업데이트		            
						// 주어진 날짜 문자열
						const originalDateString = comment.createDate;
						
						// Date 객체로 변환
						const dateObject = new Date(originalDateString);
						
						// 원하는 날짜 형식으로 포맷팅
						const formattedDate = `${dateObject.getFullYear()}/${(dateObject.getMonth() + 1).toString().padStart(2, '0')}/${dateObject.getDate().toString().padStart(2, '0')} ${dateObject.getHours().toString().padStart(2, '0')}:${dateObject.getMinutes().toString().padStart(2, '0')}`;
						

												
			            content = `
			            		<div class="row great-comment-box" >
						   			<div class="p-1 col-12" id="comment-box">
						   				<div class="d-flex justify-content-between">
						 					<div class="col-sm-11">
									 			<div id="profile">
													<div id="comment-imgbox" class="float-start" >
														<a href="사용자상세정보">
															<img id="profileimg"  src="/images/board/sample.png" alt="프로필사진">
														</a>
													</div>
													<div class="ps-5">
														<p><strong  >${comment.user.id}</strong></p>
														<input type="hidden" name="commentNo" value="${comment.no}" />
													</div>
												</div>
												<div class="commentUserInfo ps-5" >
									   					<p >${comment.content}</p>
									   					<div id="comment-info" class="d-flex justify-content-start">
									   						<p class="float-start me-2" style="font-size: 12px; color: gray" >${formattedDate}</p>
									   						<a id="btn-a-reply-" th:attrappend="id=${comment.no}" th:attr="data-comment-no=${comment.no}" href="" class="float-satrt" style="text-decoration:none; font-size: 12px; color: gray" sec:authorize="isAuthenticated()">답글쓰기</a>
									   					</div>
													
												</div>
											</div>
								   			<div class="col-sm-1 d-flex justify-content-end pt-0" sec:authorize="isAuthenticated()">
								   				
								   				<a href="/board/movie/deleteComment?no=${comment.board.no}&commentNo=${comment.no}}"
								   					class="btn btn-link btn-sm text-danger text-decoration-none"
							   						sec:authorize="isAuthenticated()"
					   		        				th:if="${id == comment.user.id}">삭제</a>
							
							   				</div>   			
										</div>
									</div>	
									<hr>
						   			<div class="new-register-box row mb-3 pt-2" id="reply-comment-box-" th:attrappend="id=${comment.no}" >
						   			</div>			
								</div>
			            
			            `
			           	$("#comment-here").append(content);
			          
						// 댓글 등록 완료 후 포커스 이동
						let newCommentElement = $("#comment-here").children().last(); // 새로 추가된 댓글 요소 선택
						
						window.scrollTo(0, newCommentElement.offset().top - 100); // 댓글 요소로 바로 이동 (100은 여유 공간 조절)

			            
			            let commentCountUpdate = parseInt($('#ajax-comment-count').text()) + 1;
			            $('#ajax-comment-count').text(commentCountUpdate);
			            $('#ajax-comment-count-2').text(commentCountUpdate);
			            

			        }
			        
			    });
			   
		    }   
		});
		
		// 대댓글 axax
		$(".new-register-box").on('click','#btn-comment' ,function() {
			
			let $that = $(this);
			
			let newContent = $("#new-content-div textarea").val();
    
		    if(newContent === "") { // 댓글 내용이 비어있을 때
		        Swal.fire({
		            icon: 'error',
		            text: '댓글 내용을 작성해주세요.',
		            onClose: () => {
		                $("#new-content-div textarea").focus(); // 입력 필드로 포커스 이동
		            }
		        });
		    } else { // 댓글 내용이 비어있지 않을 때
		        // AJAX 요청을 보내고 새로운 댓글 목록을 받아옴
			    $.ajax({
			        url: '/board/movie/addReComment',
			        method: "POST",
			        data: $("#re-form-comment").serialize(),
			        success: function(comment) {
						
						 $("#new-content-div textarea").val('');
						 
						  // 성공 시 새로운 댓글 목록을 업데이트		            
						// 주어진 날짜 문자열
						const originalDateString = comment.createDate;
						
						// Date 객체로 변환
						const dateObject = new Date(originalDateString);
						
						// 원하는 날짜 형식으로 포맷팅
						const formattedDate = `${dateObject.getFullYear()}/${(dateObject.getMonth() + 1).toString().padStart(2, '0')}/${dateObject.getDate().toString().padStart(2, '0')} ${dateObject.getHours().toString().padStart(2, '0')}:${dateObject.getMinutes().toString().padStart(2, '0')}`;
						

						
			            // 성공 시 새로운 댓글 목록을 업데이트		            
			           	let thisCommentNo = $(".re-comment-here").attr('id');
			           	let result = thisCommentNo.substr(16);
			           	
			           	let content = `
			           	<div class="p-1 offset-1 col-11" id="comment-box" >
			   				<div class="d-flex justify-content-between" id="child-comment-here">
			 					<div class="col-sm-11">
						 			<div id="profile">
										<div id="comment-imgbox" class="float-start" >
											<a href="사용자상세정보">
												<img id="profileimg" src="/images/board/sample.png" alt="프로필사진">
											</a>
										</div>
										<div class="ps-5">
											<p ><strong class="fs-6" >${comment.user.id}</strong></p>
										</div>
									</div>
									<div class="re-commentUserInfo ps-5" >
						   					<p><strong >${comment.parent.user.id} </strong> <span >${comment.content}</span></p>
						   					<div id="comment-info" class="d-flex justify-content-start">
						   						<p class="float-start me-2" style="font-size: 12px; color: gray">${formattedDate}</p>
						   						<!-- 
						   						<a id="a-re-reply" href="" class="float-satrt" style="text-decoration:none; font-size: 12px; color: gray" sec:authorize="isAuthenticated()">답글쓰기</a>
						   						-->
						   					</div>
										
									</div>
								</div>
					   			<div class="col-sm-1 d-flex justify-content-end pt-0" sec:authorize="isAuthenticated()">
					   				<input type="hidden" name="userId" value="${id}">
					   				<a th:href="@{/board/movie/deleteComment?no=${comment.no}, commentNo=${comment.no}}" 
					   					class="btn btn-link btn-sm text-danger text-decoration-none"
					   					sec:authorize="isAuthenticated()"
		   		        				th:if="${id== comment.user.id}">삭제</a>
				
				   				</div>   			
							</div>
							<hr>
				   			<div class="row mb-3 pt-2" id="re-reply-comment-box">
				   			</div>
						</div>`
						
						$that.closest('.comment-box').find('.re-comment-here').append(content);
						 // 답글 작성 폼을 제거하고 답글 쓰기 버튼으로 변경
                		$("#reply-form-" + result).remove();
            		    $("#btn-a-re-reply-" + result).attr('id', 'btn-a-reply-' + result).text('답글쓰기');
            
            			// 댓글 등록 완료 후 포커스 이동
						let newCommentElement = $(".re-comment-here").children().last(); // 새로 추가된 댓글 요소 선택
						
						window.scrollTo(0, newCommentElement.offset().top - 100); // 댓글 요소로 바로 이동 (100은 여유 공간 조절)			
            
			           	let commentCountUpdate = parseInt($('#ajax-comment-count').text()) + 1;
			            $('#ajax-comment-count').text(commentCountUpdate);
			            $('#ajax-comment-count-2').text(commentCountUpdate);
			        }
			    });
		    }   
		});
		
      const quickMenu = $("#quick-scroll");
      const triggerOffset = 700; // 퀵 메뉴를 나타낼 스크롤 위치

      // 페이지 로딩 시 퀵 메뉴 숨김
      quickMenu.hide();

      $(window).on("scroll", function () {
        const currentScroll = $(this).scrollTop();

        if (currentScroll >= triggerOffset) {
          quickMenu.show();
        } else {
          quickMenu.hide();
        }

        // 스크롤이 맨 아래까지 내려간 경우
        if (currentScroll + $(window).height()+30 > $(document).height()) {
          quickMenu.css("bottom", '30%');
        } else {
          quickMenu.css("bottom", "0%");
        }
      });
      
		
		$("#quick-scroll").on("click", function () {
        $("html, body").animate({ scrollTop: 0 }, "fast");
      });

})

		