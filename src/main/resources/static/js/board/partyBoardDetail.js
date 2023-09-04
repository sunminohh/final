
$(function () {
	
	const $request = $("input[name=request]");
	let join;
	$("#request-button").click(function(event) {
          event.preventDefault();
          let type= $(this).attr("data-button-type");
          if (type == 'plus') {
             join = 'Y';  
             
         } else if (type == 'minus') {
            join = 'N';
         }
          $request.val(join);  
          
          // AJAX 요청
          $.ajax({
              url: '/board/party/changeRequest',
              method: "POST",
              data: $("#request-btn-form").serialize(),
              success: function(response) {
                  // 성공 시 업데이트된 내용을 특정 부분에 적용
                  if (type == 'plus') {
		             $("#request-button").text('신청취소').attr('data-button-type', 'minus').removeClass('btn-outline-success').addClass('btn-success')      
               } else if (type == 'minus') {
		            $("#request-button").text('파티신청').attr('data-button-type', 'plus').removeClass('btn-success').addClass('btn-outline-success')        
               }
              }
          });
      
      });
      
      $("#party-join-btn").on('click', function() {
		  $("#join-modal").modal('show');
	  })
	  
		$(".join-box").on('click', '#join-btn', function() {
		    const id = $(this).siblings('input[name=partyId]').val(); // 수정: find -> siblings
		    const no = $(this).siblings('input[name=boardNo]').val(); // 불필요한 부분 삭제
		    const acceptCount  = $('input[name=acceptCount]').attr('value');
			const headCount = $('input[name=headCount]').val();
		    
		    if (acceptCount === headCount){
						 Swal.fire({
				            icon: 'error',
				            text: '수락할 수 있는 인원수를 초과했습니다.' ,
				            
				        });
					} else {
		    
				    $.ajax({
				        url: '/board/party/join',
				        method: "POST",
				        data: {
				            partyId: id,
				            boardNo: no // 수정: 변수 boardNo 사용
				        },
				        success: function(JoinList) {
							
							console.log("acceptCount:", acceptCount);
							console.log("headCount:", headCount);
							
				            const acceptedJoins = JoinList.acceptedJoins;
				            const notAcceptedJoins = JoinList.notAcceptedJoins;
				            const newAcceptCount = JoinList.acceptCount;
							
							$("input[name=acceptCount]").attr("value", newAcceptCount);
				            let notAcceptHtmlContent = "";
				            let acceptHtmlContent = "";
		
							
								if(notAcceptedJoins.length !== 0){
						            notAcceptedJoins.forEach(function(notAccept) {
											
						                notAcceptHtmlContent += `
						                	<div id="join-box" class="d-flex justify-content-between">
							                    <p class="me-1">${notAccept.user.id}</p>
							                    <button type="button" class="btn btn-sm btn-outline-dark" id="join-btn" style="height: 23px; font-size: 10px">수락</button>
							                    <input type="hidden" id="party-id" name="partyId" value="${notAccept.user.id}">
							                    <input type="hidden" name="boardNo" value="${no}">
						                	</div>
						                `;
						            })
								} else if (notAcceptedJoins.length === 0){
									notAcceptHtmlContent += `
											<div class="text-center" >
					                            <p>신청자가 없습니다.</p>
					                        </div> 
									`
								}
									
					
								if (acceptedJoins.length !== 0){
					            	acceptedJoins.forEach(function(accept) {
						                acceptHtmlContent += `
						                    <div id="resetJoin-box" class="d-flex justify-content-between">
							                    <p id="accepted-id">${accept.user.id}</p>
							                    <button type="button" class="btn btn-sm btn-outline-secondary" id="reset-join-btn" style="height: 23px; font-size: 10px">취소</button>
							                    <input type="hidden" name="partyId" value="${accept.user.id}">
							                    <input type="hidden" name="boardNo" value="${no}">
						                	</div>
						                `;
					            	})
						                
								}else if(acceptedJoins.length === 0){
									acceptHtmlContent += `			                            	
											<div class="text-center" >
				                            		<p>수락된 신청자가 없습니다.</p>
				                            </div>   `
								}
					
					           $("#join-form").empty().append(notAcceptHtmlContent);
			          		   $("#resetJoin-form").empty().append(acceptHtmlContent);
			          		   }
				    });
		    }
		});

	  $(".reject-box").on('click', '#reset-join-btn', function() {
	
			const id = $(this).siblings('input[name=partyId]').val(); // 수정: find -> siblings
		    const no = $(this).siblings('input[name=boardNo]').val(); // 불필요한 부분 삭제
		    
			$.ajax({
		        url: '/board/party/resetJoin',
		        method: "POST",
		        data: {
		            partyId: id,
		            boardNo: no // 수정: 변수 boardNo 사용
		        },
		        success: function(JoinList) {
		            const acceptedJoins = JoinList.acceptedJoins;
		            const notAcceptedJoins = JoinList.notAcceptedJoins;
		            const newAcceptCount = JoinList.acceptCount;
							
							
					$("input[name=acceptCount]").attr("value", newAcceptCount);
		
		            let notAcceptHtmlContent = "";
		            let acceptHtmlContent = "";
		
					if(notAcceptedJoins.length != 0){
			            notAcceptedJoins.forEach(function(notAccept) {
							
			                notAcceptHtmlContent += `
			                	<div id="join-box" class="d-flex justify-content-between">
				                    <p class="me-1">${notAccept.user.id}</p>
				                    <button type="button" class="btn btn-sm btn-outline-dark" id="join-btn" style="height: 23px; font-size: 10px">수락</button>
				                    <input type="hidden" id="party-id" name="partyId" value="${notAccept.user.id}">
				                    <input type="hidden" name="boardNo" value="${no}">
			                	</div>
			                `;
			        	})
			        } else if (notAcceptedJoins.length == 0){
						notAcceptHtmlContent += `
								<div class="text-center" >
		                            <p>신청자가 없습니다.</p>
		                        </div> 
						`
					}
					
					if (acceptedJoins.length != 0){
			            acceptedJoins.forEach(function(accept) {
			                acceptHtmlContent += `
			                    <div id="resetJoin-box" class="d-flex justify-content-between">
				                    <p id="accepted-id">${accept.user.id}</p>
				                    <button type="button" class="btn btn-sm btn-outline-secondary" id="reset-join-btn" style="height: 23px; font-size: 10px">취소</button>
				                    <input type="hidden" name="partyId" value="${accept.user.id}">
				                    <input type="hidden" name="boardNo" value="${no}">
			                	</div>
			                `;
			            })
					}else if(acceptedJoins.length == 0){
						acceptHtmlContent += `			                            	
							<div class="text-center" >
                            		<p>수락된 신청자가 없습니다.</p>
                            </div>`
					}
		
		            $("#join-form").empty().append(notAcceptHtmlContent);
            		$("#resetJoin-form").empty().append(acceptHtmlContent);
		        }
		    });
	  })
	  
	  $("#party-complete-btn").on('click', function() {
		  const no = $('input[name=no]').val(); 
        
		  Swal.fire({
	           icon: 'warning',
	           title: '정말 마감하시겠습니까? ',
	           text: '마감후 신청자를 받을 수 없습니다.',
	           showCancelButton: true,
	           confirmButtonText: '네',
	           cancelButtonText: '아니오',
	       }).then((result) => {
	           if (result.isConfirmed) {
				  $.ajax({
				        url: '/board/party/partyComplete',
				        method: "POST",
				        data: {
				            no: no
				        },
				        success: function() {
							 Swal.fire({
					            icon: 'info',
					            text: '마감되었습니다.' 
					            
					        	});
					        	$(".reject-box #reset-join-btn").remove();
					        	$(".join-box #join-btn").remove();
					        	$("#party-complete-btn").remove();
					        	$("#modify-btn").remove();
							}
			 		    })
	           } else if (result.dismiss === Swal.DismissReason.cancel) {
	               
	           }
	       });
	  })
	  
	  // 삭제 버튼
   	$("#delete-btn").on("click", function(event) {
      event.preventDefault();
      let no = $('[name="no"]').val();
      
       Swal.fire({
           icon: 'warning',
           title: '정말 삭제하시겠습니까?',
           showCancelButton: true,
           confirmButtonText: '네',
           cancelButtonText: '아니오',
	       }).then((result) => {
	           if (result.isConfirmed) {
	                 window.location.href = 'delete?no=' + no;      
	           } else if (result.dismiss === Swal.DismissReason.cancel) {
	               
	           }
	       });
	   });
	
	   $("#link-btn").on("click", function() {
	       // 현재 페이지의 URL을 클립보드에 복사
	       navigator.clipboard.writeText(window.location.href)
	           .then(() => {
	               // 복사 성공 시 팝오버를 표시합니다.
	               $("#link-btn").popover('show');
	               
	               // 2초 후에 팝오버를 숨깁니다.
	               setTimeout(function() {
	                   $("#link-btn").popover('hide');
	               }, 2000);
	           })
	           .catch((error) => {
	               console.error("URL 복사 중 오류가 발생했습니다.", error);
	               // 복사 실패 시에 수행할 작업을 여기에 추가할 수 있습니다.
	           });
	   });
	   
	   // 페이지 로딩 시에 팝오버 초기화
	   $(function () {
	       $('[data-bs-toggle="popover"]').popover();
	   });
	   
	   
	  $("#report-submit-btn").on("click", function(event) {
	      event.preventDefault();
	      let content = document.querySelector("textarea[name=reasonContent]").value;
	      let reasonNo = document.querySelector("select[name=reasonNo]").value;
	      if(reasonNo === '24' && content === ''){
	         Swal.fire({
	               icon: 'error',
	               text: `'기타'선택시 추가 정보 입력이 필요합니다.`,
	           });
	      }  else if(reasonNo === '') {
	         Swal.fire({
	               icon: 'error',
	               text: `신고이유를 선택해주세요.`,
	           });
	      } else {
	           Swal.fire({
	           icon: 'warning',
	           title: '정말 신고하시겠습니까?',
	           showCancelButton: true,
	           confirmButtonText: '네',
	           cancelButtonText: '아니오',
		       }).then((result) => {
		           if (result.isConfirmed) {
						$("#report-form").submit();
		           } else if (result.dismiss === Swal.DismissReason.cancel) {
		               
		           }
		       });
	      }
   	  })
   	  
   	  // 답글 작성 버튼을 클릭하면
	    $("#all-comment-box").on("click", "[data-comment-no]", function (event) {
	        event.preventDefault();

       		let commentNo = $(this).attr("data-comment-no");
       		const writerId = $("input[name=writerId]").val();
			const greatCommentId = $(this).closest(".great-box").find("#greatCommentId").text();

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
	                    <form id="re-form-comment" method="post" action="/board/party/addReComment">
	                        <input type="hidden" name="no" value="${no}" />
	                        <input type="hidden" name="parentNo" value="${commentNo}" />
	                        <input type="hidden" name="greatNo" value="${commentNo}" />
							<input type="hidden" name="id" value=${loginId} />
	                        <input type="hidden" name="writerId" value=${writerId} />
							<input type="hidden" name="greatCommentId" value=${greatCommentId} />
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
		    $(".great-box").on("click", "[data-cancel-comment]", function (event) {
		        event.preventDefault();
	
	
	        $("#reply-form-" + commentNo).remove();
	        $("#btn-a-re-reply-" + commentNo).attr('id', 'btn-a-reply-' + commentNo).text('답글쓰기');
	    });
	    
	    
	  
      
   	  
      let no = document.querySelector("input[name=no]").value;
      let loginId = $("#login-id").attr("value");
      let commentNo = $(this).attr("data-cancel-comment");
      
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
                 url: '/board/party/addComment',
                 method: "POST",
                 data: $("#form-comment").serialize(),
                 success: function(commentList) {
                     $("#content-box textarea").val('');
                       
                  const parents = commentList.parentComments;
                  const childs = commentList.childComments;
                  // 성공 시 새로운 댓글 목록을 업데이트                  
                  let content = `<div class="comment-box">`;
				  let childContent;
                    parents.forEach(function(comment) {
	                  // 주어진 날짜 문자열
	                  const originalDateString = comment.createDate;
	                  
	                  // Date 객체로 변환
	                  const dateObject = new Date(originalDateString);
	                  
	                  // 원하는 날짜 형식으로 포맷팅
	                  const formattedDate = `${dateObject.getFullYear()}/${(dateObject.getMonth() + 1).toString().padStart(2, '0')}/${dateObject.getDate().toString().padStart(2, '0')} ${dateObject.getHours().toString().padStart(2, '0')}:${dateObject.getMinutes().toString().padStart(2, '0')}`;
	                  
						
                      content += `
                      <div class="great-box">
                      	<div class="row great-comment-box pt-1" >
                              <div class="p-1 col-12" id="comment-box">
                                 <div class="d-flex justify-content-between">
                                  <div class="col-sm-11">
                                     <div id="profile">
                                       <div id="comment-imgbox" class="float-start" >
                                          <a href="/board/user/list?id=${comment.user.id}">
                                             <img id="profileimg"  src="/images/board/sample.png" alt="프로필사진">
                                          </a>
                                       </div>
                                       <div class="ps-5">
                                          <p><strong id="greatCommentId">${comment.user.id}</strong></p>
                                          <input type="hidden" name="greatCommentNo" value="${comment.no}" />
                                       </div>
                                    </div>
                                    <div class="commentUserInfo ps-5" >
                                             <p >${comment.content}</p>
                                             <div id="comment-info" class="d-flex justify-content-start">
                                                <p class="float-start me-2" style="font-size: 12px; color: gray" >${formattedDate}</p>
                                                <a id="btn-a-reply-${comment.no}" data-comment-no=${comment.no} href="" class="reply-btn float-start" style="text-decoration:none; font-size: 12px; color: gray" sec:authorize="isAuthenticated()">답글쓰기</a>
                                             </div>
                                       
                                    </div>
                                 </div>
                                    <div class="col-sm-1 d-flex justify-content-end pt-0" sec:authorize="isAuthenticated()">
                                       
                                       <a href="/board/party/deleteGreatComment"
                                          class="btn btn-link btn-sm text-danger text-decoration-none"
                                          sec:authorize="isAuthenticated()"
                                            th:if="${id == comment.user.id}"
                                            id="delete-comment-btn">삭제</a>
                     
                                    </div>            
                              </div>
                           </div>   
                           <hr>
                        <div class="re-comment-here" id="re-comment-here-${comment.no}">
                  		</div>
                              <div class="new-register-box  row mb-3 pt-2" id="reply-comment-box-${comment.no}" >
                              
                              </div>
                        </div>
                     </div>
   					  
                     `
                     childs.forEach(function(child){
						 
						 if (childs !== null && comment.no == child.great.no){
						 content += `
						  <div class="re-comment-box-loop" id="re-comment-box-${comment.no}">
							  <div class=" row child-comment-box">
							 	<div class="p-1 offset-1 col-11 pt-2" id="comment-box" >
			                        <div class="d-flex justify-content-between" id="child-comment-here">
			                         <div class="col-sm-11">
			                            <div id="profile">
			                              <div id="comment-imgbox" class="float-start" >
			                                 <a href="/board/user/list?id=${child.user.id}">
			                                    <img id="profileimg" src="/images/board/sample.png" alt="프로필사진">
			                                 </a>
			                              </div>
			                              <div class="ps-5">
			                                 <p ><strong class="fs-6" >${child.user.id}</strong></p>
			                                 <input type="hidden" name="commentNo" value="${child.no}" />
			                              </div>
			                           </div>
			                           <div class="re-commentUserInfo ps-5" >
			                                    <p><strong >${child.parent.user.id} </strong> <span >${child.content}</span></p>
			                                    <div id="comment-info" class="d-flex justify-content-start">
			                                       <p class="float-start me-2" style="font-size: 12px; color: gray">${formattedDate}</p>
			                                       <!-- 
			                                       <a id="a-re-reply" href="" class="reply-btn float-satrt" style="text-decoration:none; font-size: 12px; color: gray" sec:authorize="isAuthenticated()">답글쓰기</a>
			                                       -->
			                                    </div>
			                              
			                           </div>
			                        </div>
			                           <div class="col-sm-1 d-flex justify-content-end pt-0" sec:authorize="isAuthenticated()">
			                              <input type="hidden" name="userId" value="${id}">
			                              <a href="/board/party/deleteReComment" 
			                                 class="btn btn-link btn-sm text-danger text-decoration-none"
			                                 sec:authorize="isAuthenticated()"
			                                   th:if="${id== child.user.id}"
			                                   id="delete-reComment-btn">삭제</a>
			            
			                           </div>            
			                     </div>
			                     <hr>
			                        <div class="row mb-3 pt-2 " id="re-reply-comment-box">
			                        </div>
			                  </div>
			                 </div>
			                </div>
						 `
						 }
					 })
					 content += `</div>`
					});                
                       $("#all-comment-box").html(content);
                   			

                     
                     let commentCountUpdate = parseInt($('#ajax-comment-count').text()) + 1;
                     $('#ajax-comment-count').text(commentCountUpdate);
                     $('#ajax-comment-count-2').text(commentCountUpdate);
                     
                     let newCommentElement = $("#all-comment-box").children().last();
					 window.scrollTo(0, newCommentElement.offset().top - 100); // 댓글 요소로 바로 이동 (100은 여유 공간 조절)

                 }
                 
             });
            
          }   
      });
      
      
      $("#all-comment-box").on('click','#btn-comment' ,function() {
         
         let $that = $(this);
         let inputcontent = $("#new-content-div textarea").val();
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
          
             let thisCommentNo = $(".re-comment-here").attr('id');
             let result = thisCommentNo.substr(16);
             $.ajax({
                 url: '/board/party/addReComment',
                 method: "POST",
                 data: $("#re-form-comment").serialize(),
                 success: function(commentList) {
                     $("#content-box textarea").val('');
                       
                  const parents = commentList.parentComments;
                  const childs = commentList.childComments;
                  // 성공 시 새로운 댓글 목록을 업데이트                  
                  let content = `<div class="comment-box">`;
                    parents.forEach(function(comment) {
	                  // 주어진 날짜 문자열
	                  const originalDateString = comment.createDate;
	                  
	                  // Date 객체로 변환
	                  const dateObject = new Date(originalDateString);
	                  
	                  // 원하는 날짜 형식으로 포맷팅
	                  const formattedDate = `${dateObject.getFullYear()}/${(dateObject.getMonth() + 1).toString().padStart(2, '0')}/${dateObject.getDate().toString().padStart(2, '0')} ${dateObject.getHours().toString().padStart(2, '0')}:${dateObject.getMinutes().toString().padStart(2, '0')}`;
						
                      content += `
                      <div class="great-box">
                      	<div class="row great-comment-box pt-1" >
                              <div class="p-1 col-12" id="comment-box">
                                 <div class="d-flex justify-content-between">
                                  <div class="col-sm-11">
                                     <div id="profile">
                                       <div id="comment-imgbox" class="float-start" >
                                          <a href="/board/user/list?id=${comment.user.id}">
                                             <img id="profileimg"  src="/images/board/sample.png" alt="프로필사진">
                                          </a>
                                       </div>
                                       <div class="ps-5">
                                          <p><strong id="greatCommentId">${comment.user.id}</strong></p>
                                          <input type="hidden" name="greatCommentNo" value="${comment.no}" />
                                       </div>
                                    </div>
                                    <div class="commentUserInfo ps-5" >
                                             <p >${comment.content}</p>
                                             <div id="comment-info" class="d-flex justify-content-start">
                                                <p class="float-start me-2" style="font-size: 12px; color: gray" >${formattedDate}</p>
                                                <a id="btn-a-reply-${comment.no}" data-comment-no=${comment.no} href="" class="reply-btn float-start" style="text-decoration:none; font-size: 12px; color: gray" sec:authorize="isAuthenticated()">답글쓰기</a>
                                             </div>
                                       
                                    </div>
                                 </div>
                                    <div class="col-sm-1 d-flex justify-content-end pt-0" sec:authorize="isAuthenticated()">
                                       
                                       <a href="/board/party/deleteGreatComment"
                                          class="btn btn-link btn-sm text-danger text-decoration-none"
                                          sec:authorize="isAuthenticated()"
                                            th:if="${id == comment.user.id}"
                                            id="delete-comment-btn">삭제</a>
                     
                                    </div>            
                              </div>
                           </div>   
                           <hr>
                        <div class="re-comment-here" id="re-comment-here-${comment.no}">
                  		</div>
                              <div class="new-register-box  row mb-3 pt-2" id="reply-comment-box-${comment.no}" >
                              
                              </div>
                        </div>
                     </div>
   					  
                     `
                     childs.forEach(function(child){
						 
						 if (childs !== null && comment.no == child.great.no){
						 content += `
						  <div class="re-comment-box-loop" id="re-comment-box-${comment.no}">
							  <div class=" row child-comment-box">
							 	<div class="p-1 offset-1 col-11 pt-2" id="comment-box" >
			                        <div class="d-flex justify-content-between" id="child-comment-here">
			                         <div class="col-sm-11">
			                            <div id="profile">
			                              <div id="comment-imgbox" class="float-start" >
			                                 <a href="/board/user/list?id=${child.user.id}">
			                                    <img id="profileimg" src="/images/board/sample.png" alt="프로필사진">
			                                 </a>
			                              </div>
			                              <div class="ps-5">
			                                 <p ><strong class="fs-6" >${child.user.id}</strong></p>
			                                 <input type="hidden" name="commentNo" value="${child.no}" />
			                              </div>
			                           </div>
			                           <div class="re-commentUserInfo ps-5" >
			                                    <p><strong >${child.parent.user.id} </strong> <span >${child.content}</span></p>
			                                    <div id="comment-info" class="d-flex justify-content-start">
			                                       <p class="float-start me-2" style="font-size: 12px; color: gray">${formattedDate}</p>
			                                       <!-- 
			                                       <a id="a-re-reply" href="" class="reply-btn float-satrt" style="text-decoration:none; font-size: 12px; color: gray" sec:authorize="isAuthenticated()">답글쓰기</a>
			                                       -->
			                                    </div>
			                              
			                           </div>
			                        </div>
			                           <div class="col-sm-1 d-flex justify-content-end pt-0" sec:authorize="isAuthenticated()">
			                              <input type="hidden" name="userId" value="${id}">
			                              <a href="/board/party/deleteReComment" 
			                                 class="btn btn-link btn-sm text-danger text-decoration-none"
			                                 sec:authorize="isAuthenticated()"
			                                   th:if="${id== child.user.id}"
			                                   id="delete-reComment-btn">삭제</a>
			            
			                           </div>            
			                     </div>
			                     <hr>
			                        <div class="row mb-3 pt-2 " id="re-reply-comment-box">
			                        </div>
			                  </div>
			                 </div>
			                </div>
						 `
						 }
					 })
 					 content += `</div>`
					});                
                       $("#all-comment-box").html(content);
                   			
					     $that.closest('.comment-box').find('.re-comment-here').append(content);
                  
                   //답글 작성 폼을 제거하고 답글 쓰기 버튼으로 변경
	                 $that.closest('.great-box').find('.new-register-box ').remove();
	                 $that.closest('.comment-box').find(".reply-btn").attr('id', 'btn-a-reply-' + result).text('답글쓰기');
	            
                     let commentCountUpdate = parseInt($('#ajax-comment-count').text()) + 1;
                     $('#ajax-comment-count').text(commentCountUpdate);
                     $('#ajax-comment-count-2').text(commentCountUpdate);
                     
                 }
                 
             });
            
          }   
      });
      
      // 댓글 삭제
      $("#all-comment-box").on('click', '#delete-comment-btn', function(event) {
		    event.preventDefault();
		    let $that = $(this);
		    let thisCommentNo = $that.closest(".great-box").find('input[name=greatCommentNo]').val();
			console.log(thisCommentNo);
		    let requestData = {
		        no: $("input[name=no]").val(),
		        greatCommentNo: $that.closest('.great-box').find("input[name=greatCommentNo]").val()
		    };
		
		
		    Swal.fire({
		        icon: 'warning',
		        title: '정말 삭제하시겠습니까?',
		        showCancelButton: true,
		        confirmButtonText: '네',
		        cancelButtonText: '아니오',
		    }).then((result) => {
		        if (result.isConfirmed) {
		            $.ajax({
		                url: '/board/party/deleteGreatComment', // 변경된 엔드포인트
		                method: 'POST',
		                contentType: 'application/json',
		                data: JSON.stringify(requestData),
		                success: function(commentCount) {
		                    // 삭제 후의 댓글 수를 받아와서 업데이트
		                    $('#ajax-comment-count').text(commentCount);
		                    $("#ajax-comment-count-2").text(commentCount);
		                    $that.closest('.great-box').remove();
		                    $("[id^="+'re-comment-box-'+thisCommentNo+"]").remove();
		                }
		            });
		        } else if (result.dismiss === Swal.DismissReason.cancel) {
		            // 취소 버튼 클릭 시
		        }
		    });
		});
      
      // 대댓글 삭제
      $("#all-comment-box").on('click', '#delete-reComment-btn', function(event) {
          event.preventDefault();
          let $that = $(this);
      
             // 현재 클릭된 .child-comment-box 내부에서 name이 "commentNo"인 hidden input 요소 선택
             let commentNoInput = $that.closest(".child-comment-box").find("input[type='hidden'][name='commentNo']");
             let commentNo = commentNoInput.val();
             
             let requestData = {
                 no: $("input[name=no]").val(),
                 commentNo: commentNo
             };
      
          Swal.fire({
              icon: 'warning',
              title: '정말 삭제하시겠습니까?',
              showCancelButton: true,
              confirmButtonText: '네',
              cancelButtonText: '아니오',
          }).then((result) => {
              if (result.isConfirmed) {
                  $.ajax({
                      url: '/board/party/deleteReComment', // 변경된 엔드포인트
                      method: 'POST',
                      contentType: 'application/json',
                      data: JSON.stringify(requestData),
                      success: function(commentCount) {
                          // 삭제 후의 댓글 수를 받아와서 업데이트
                          $('#ajax-comment-count').text(commentCount);
                          $("#ajax-comment-count-2").text(commentCount);
                          $that.closest(".child-comment-box").find("#comment-box").remove();
                      }
                  });
              } else if (result.dismiss === Swal.DismissReason.cancel) {
                  // 취소 버튼 클릭 시
              }
          });
      });
      
      
      // 퀵메뉴 (위로)
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

    function toggleContentField() {
        // 신고 모달 - '기타' 선택시에만 상세내용 활성화
        let el = document.querySelector("textarea[name=reasonContent]");
        let reasonNo = document.querySelector("select[name=reasonNo]").value;
        
        if (reasonNo === '24') { 
            el.disabled = false;
        } else {
            el.disabled = true;
        }
        
        console.log(reasonNo);
    }