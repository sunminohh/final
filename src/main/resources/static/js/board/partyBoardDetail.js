
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
							                    <input type="hidden" name="partyId" value="${notAccept.user.id}">
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
				                    <input type="hidden" name="partyId" value="${notAccept.user.id}">
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