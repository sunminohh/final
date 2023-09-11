$(function() {
    
    // 댓글 추가
    $("#form-comment").submit(function(e) {
         e.preventDefault();
         let inputcontent = $("#commentArea").val();
      
          if(inputcontent === "") { // 댓글 내용이 비어있을 때
              Swal.fire({
                  icon: 'error',
                  text: '답변 내용을 작성해주세요.',
              });
          } else { // 댓글 내용이 비어있지 않을 때
             // AJAX 요청을 보내고 새로운 댓글 목록을 받아옴
             $.ajax({
                 url: '/admin/support/one/addComment',
                 method: "POST",
                 data: $("#form-comment").serialize(),
                 success: function(comments) {
                     // 성공 시 새로운 댓글 목록을 업데이트
                     const html = comments.map(({content, no}, i) =>
                         `<div class="cont review">
                             <p>${content.replace(/\n/g, '<br>')}</p>
                             <div class="col-sm-1 d-flex justify-content-end pt-0 float-end" ">
                          		<a class="btn btn-link btn-sm text-danger text-decoration-none delete-comment-btn"
                          		href="/admin/support/one/deleteComment"
                          		sec:authorize="isAuthenticated()"
                            	data-comment-no="${no}">삭제</a>
                      	 	</div>       
                         </div>`
                     ).join("\n");
                     $(".review").remove();
                     $("#reviewBox").append(html);
                     $("#commentArea").val("");
                     Swal.fire({
						 icon: 'success',
		                 text: '답변이 등록되었습니다.',
					});
                 }
             });
          }   
      });
      
      
        // 댓글 삭제
	    $(".board-view").on("click", '.delete-comment-btn', function(e) {
	        e.preventDefault();
	
	        // 댓글 번호 가져오기
	        const commentNo = $(this).data("comment-no");
	
	        // 삭제 요청을 서버로 보내기
	        $.ajax({
	            type: "POST",
	            url: "/admin/support/one/deleteComment",
	            data: { commentNo: commentNo }, // 댓글 번호를 서버에 전달
	            success: function(response) {
	                // 삭제 요청이 성공하면 해당 댓글 박스를 제거
	                $("#reviewBox").find(`[data-comment-no="${commentNo}"]`).closest(".cont").remove();
	            },
	            error: function() {
	                // 삭제 요청이 실패하면 오류 처리
	               Swal.fire({
	                  icon: 'error',
	                  text: '오류가 발생했습니다.',
	          		});
	            }
	        });
	    });

})











