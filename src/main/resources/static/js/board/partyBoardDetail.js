
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
		    const acceptCount  = $('input[name=acceptCount]').attr('th:value');
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
							
							$("input[name=acceptCount]").attr("th:value", newAcceptCount);
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
							
							
					$("input[name=acceptCount]").attr("th:value", newAcceptCount);
		
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
})