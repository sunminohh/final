$(() => {
	
	// 댓글 추가
	$("#form-comment").submit(function (e) {
		e.preventDefault();
		let inputcontent = $("#commentArea").val();	
		
		if(inputcontent === "") {
			Swal.fire({
				 icon: 'error',
                 text: '답변 내용을 작성해주세요.',
			});
		} else {
			$.ajax({
				url: '/admin/support/lost/addComment',
				method: "POST",
				data: $("#form-comment").serialize(),
				success: function(comments) {
					const html = comments.map(({content, no}, i) => 
						  `<div class="cont review">
	                        <p>${content}</p>
	                         <div class="col-sm-1 d-flex justify-content-end pt-0 float-end" ">
	                      		<a class="btn btn-link btn-sm text-danger text-decoration-none delete-comment-btn"
	                      		href="/admin/support/lost/deleteComment"
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
		
		const commentNo = $(this).data("comment-no");
		
		$.ajax({
			type: "POST",
			url: "/admin/support/lost/deleteComment",
			data: { commentNo: commentNo },
			success: function(reponsse) {
				$("#reviewBox").find(`[data-comment-no="${commentNo}"]`).closest(".cont").remove();
			},
			error: function() {
				Swal.fire({
                  icon: 'error',
                  text: '오류가 발생했습니다.',
          		});
			}
			
		})
	})
	
	
	
})









