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
                    	 <h1 class="modal-title fs-5 fw-semibold" id="exampleModalLabel" style="color: white;">${result.list.name}</h1>
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
