$(function() {
	
	  $("#report-table").on('click', '#report-btn', function() {
		  
		 const no = $(this).closest('#board-tr').find("input[name=boardNo]").val();
		 const type = $(this).closest('#board-tr').find("input[name=boardType]").val();
			  
		  $("input[name=no]").attr('value', no);
		  $("input[name=type]").attr('value', type);
		  
		  $.ajax({
                 url: '/admin/board/boardDetail',
                 method: "GET",
                 data: $("#board-detail").serialize(),
                 success: function(result) {
                    let boardContent = `<p id="board-content">${result.list.content}</p>`
                    $("#board-info").empty().append(boardContent);
                    
                    let boardName = `
                    	<div class="col-11">
                    	 <h1 class="modal-title fs-5 fw-semibold" id="exampleModalLabel" style="color: white;">${result.list.name}</h1>
                    	</div>
                    	<div class="col-1"> 
                    	 <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" style="position: absolute; top: 15px; right: 15px;"></button>
                    	</div>
                    `
                    $('.modal-header').empty().append(boardName)
                    
                    let reports = result.reports;
                    let reportContent ="";
                    reports.forEach(function(report) {
						reportContent += `
						<div class="report-reason d-flex justify-content-start  row">
	               	   		<div class="pe-2 col-2 fw-semibold">${report.reasonName}</div>
	               	   	`
	               	   	if(report.reason != null){
	               	   	reportContent += 
	               	   	`
	               	   		<div class="col-10">${report.reason}</div>
	               	    `
	               	    }
	               	   reportContent += `
	               	  	</div>
	           	   		<hr class="mt-3">
						`
					})
					$(".report-box").empty().append(reportContent);
                 }
             });
		  
		  $("#board-modal").modal('show');
	  })
	  
	$(".btn-box").on('click', '#delete-btn', function() {
	    const type = $("input[name=type]").val();
	    const no = $("input[name=no]").val();
	    
	           Swal.fire({
           icon: 'warning',
           title: '정말 삭제하시겠습니까?',
           showCancelButton: true,
           confirmButtonText: '네',
           cancelButtonText: '아니오',
       }).then((result) => {
           if (result.isConfirmed) {
			    $.ajax({
			        url: '/admin/board/delete',
			        type: 'POST', 
			        data: {
			            type: type,
			            no: no
			        },
			        success: function(totalRows) {
						    if ($("input[name=boardNo]").val() === no && $("input[name=boardType]").val() === type) {
						        const boardTr = $("#board-tr");
						        boardTr.remove();
						    }
						    $("#total-rows").text(totalRows);
						    $("#board-modal").modal('hide');
							if(totalRows === 0){
								let content = `
								<tr>
									<td colspan="5">게시글이 존재하지 않습니다.</td>
								</tr>	
								`	
								$("#board-tbody").empty().append(content);
								$("#board-nav").remove();							
							}

			        },
			        error: function(error) {
						
			        }
			    });
                    
           } else if (result.dismiss === Swal.DismissReason.cancel) {
               
           }
       });
	    
	});

	$(".btn-box").on('click', '#restore-btn', function() {
	    const type = $("input[name=type]").val();
	    const no = $("input[name=no]").val();
	    
	       Swal.fire({
           icon: 'warning',
           title: '정말 복구하시겠습니까?',
           text: '신고된 게시물 복구 시 해당 게시물의 신고 상태가 초기화되며, 신고 이유 목록이 삭제됩니다.',
           showCancelButton: true,
           confirmButtonText: '네',
           cancelButtonText: '아니오',
       }).then((result) => {
           if (result.isConfirmed) {
			    $.ajax({
			        url: '/admin/board/restore',
			        type: 'POST', 
			        data: {
			            type: type,
			            no: no
			        },
			        success: function(totalRows) {
						    if ($("input[name=boardNo]").val() === no && $("input[name=boardType]").val() === type) {
						        const boardTr = $("#board-tr");
						        boardTr.remove();
						    }
						    $("#total-rows").text(totalRows);
						    $("#board-modal").modal('hide');
						    
						    if(totalRows === 0){
								let content = `
								<tr>
									<td colspan="5">게시글이 존재하지 않습니다.</td>
								</tr>	
								`	
								$("#board-tbody").empty().append(content);
								$("#board-nav").remove();							
							}
						    
			        },
			        error: function(error) {
						
			        }
			    });
                    
           } else if (result.dismiss === Swal.DismissReason.cancel) {
               
           }
       });
	    
	});

})

	function searchBoard(){	
		let keyword = document.querySelector("input[name=keyword]").value;
		if(keyword.trim() === ""){
			alert("키워드를 입력하세요")			
			document.querySelector("input[name=page]").value = 1;
			return
		}
		
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
		
	}
	
	function changeBoards(){
		const boards = document.querySelector("select[name=boards]").value;
		document.querySelector("input[name=boards]").value = boards;
		document.querySelector("input[name=page]").value = 1;
		
		document.querySelector("#form-board-search").submit();
		
	}
