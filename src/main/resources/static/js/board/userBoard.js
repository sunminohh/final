

$(function() {
	
	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val(1);
		
		getNoticeList();
	});
	
	// 게시판 변경
	$("#select-board select").change(function() {
		const boards = $(this).val();
		$("input[name=boards]").val(boards);
		
		getNoticeList();
	})

	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		
		getNoticeList();
	});
	
	// 폼 전송 이벤트
	$("#form-board-search").on('submit', function(e) {
		e.preventDefault();
		getNoticeList();
	});

	
	// 페이저번호클릭했 떄
	$('.pagination').on('click', '.page-number-link', function(event) {
		event.preventDefault();
		let page = $(this).attr("data-page");
		
		// 모든 페이지 번호 링크에서 active 클래스 제거
        $('.page-number-link').removeClass('active');

        // 클릭한 페이지 번호에만 active 클래스 추가
        $(this).addClass('active'); 
		
		$("input[name=page]").val(page);
		
		getNoticeList();
	})	
		
	
    function getNoticeList() {
		
		let page = $("input[name=page]").val();
		let keyword = $("input[name=keyword]").val();
		let opt = $("select[name=opt]").val();
		let id = $("input[name=id]").val();
		let boards = $("input[name=boards]").val();
		
		let $tbody = $(".boardList").empty()
		let $pagination = $(".pagination").empty();
		
		$.getJSON("/board/user/ajaxList", {id:id, page:page, boards:boards, opt:opt,  keyword:keyword}, function(result) {
			
			// 총 건수 업데이트
       		$('#total-rows').text(result.pagination.totalRows);
       		
       		let boards = result.list;
       		let pagination = result.pagination;
       		
       		if (boards.length === 0) {
				   $tbody.empty().append(`
				   <tr>
					<td colspan="4">게시글이 존재하지 않습니다.</td>
				</tr>
				   `);
			} else {
				 let tbodyHtml;
				 let paginationHtml ="";
				 let href;
				 boards.forEach(function(board){
					 
					 // 주어진 날짜 문자열
	                  const originalDateString = board.createDate;
	                  
	                  // Date 객체로 변환
	                  const dateObject = new Date(originalDateString);
	                  
	                  // 원하는 날짜 형식으로 포맷팅
	                  const formattedDate = `${dateObject.getFullYear()}/${(dateObject.getMonth() + 1).toString().padStart(2, '0')}/${dateObject.getDate().toString().padStart(2, '0')} ${dateObject.getHours().toString().padStart(2, '0')}:${dateObject.getMinutes().toString().padStart(2, '0')}`;
	                 
	                  let boardName = board.name;
	                  if (boardName.length >= 48) {
						  boardName = boardName.slice(0, 48) + '...';
						}
							                   
						
						if(board.type === '영화'){
							href=`../../board/movie/detail?no=${board.no}`
						}
						if(board.type === '극장'){
							href=`../../board/theater/detail?no=${board.no}`
						}
						if(board.type === '스토어'){
							href=`../../board/store/detail?no=${board.no}`
						}
						if(board.type === '파티'){
							href=`../../board/party/detail?no=${board.no}`
						}
						
						tbodyHtml += `
						<tr >
				       	<input type="hidden" name="type" th:vlaue="${board.type}"/>
				       	<input type="hidden" name="no" th:vlaue="${board.no}"/>
		                <td style="text-align: center;" th:text="${board.type}">${board.type}</td>
		                <td>
		                	<div class="combined-cell">
			            		<a class="text-black text-decoration-none" href="${href}">
								    <div class="title-content-2">${boardName}</div>
								</a>
			                </div>
		                </td>
		                <td style="text-align: center;" >${formattedDate}</td>
		                <td style="text-align: center;" >${board.readCount}</td>
		            </tr>
					
					`	 
				 })
				
			    if (!pagination.first) {
			        paginationHtml += createPaginationItem(1, false, 'control first');
			    }
			    if (pagination.currentBlock > 1) {
			        const prePage = (pagination.currentBlock - 1) * 5;
			        paginationHtml += createPaginationItem(prePage, pagination.page === prePage, 'control prev');
			    }
			
			    for (let i = pagination.beginPage; i <= pagination.endPage; i++) {
			        paginationHtml += createPaginationItem(i, pagination.page === i);
			    }
			
			    if (pagination.currentBlock < pagination.totalBlocks) {
			        const nextPage = pagination.currentBlock * 5 + 1;
			        paginationHtml += createPaginationItem(nextPage, pagination.page === nextPage, 'control next');
			    }
			
			    if (!pagination.last) {
			        paginationHtml += createPaginationItem(pagination.totalPages, pagination.page === pagination.totalPages, 'control last');
			    }
				
				$tbody.html(tbodyHtml);
				$pagination.html(paginationHtml);
			 }
		})
    }
})

function createPaginationItem(page, isActive = false, className = '') {
    return `
        <li class="page-item">
            <a href="list?page=${page}" 
                class="page-link page-number-link ${className} ${isActive ? 'active' : ''}" 
                data-page="${page}">${page}</a>
        </li>
    `;
}
	
	