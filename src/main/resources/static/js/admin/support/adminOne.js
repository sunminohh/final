$(function() {
	
	// 탭컬러 바꾸기
	$('li.tab-link').click(function() {

	      $('li.tab-link').removeClass('current');
	      $('button.btn').removeClass('current');
	
	      $(this).addClass('current');
	      $(this).find('button.btn').addClass('current');
    });
	
	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val(1);
		
		getOneList();
	});
	
	const params = new URLSearchParams(location.search);
	const defaultKeyword = params.get('keyword');
	if (defaultKeyword) {
		$("input[name=keyword]").val(defaultKeyword);
		getOneList();
	}
	
	// 폼 전송 이벤트
	$("#actionForm").on('submit', function(e) {
		e.preventDefault();
		getOneList();
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
		
		getOneList();
	})	
	  
	// 탭을 클릭했을 때
    $("li.tab-link").click(function() {
	    let categoryNo = $(this).find('button').attr("data-category-no");
		$("input[name=categoryNo]").val(categoryNo);
		$("input[name=page]").val(1);
		
		getOneList();
    });
    
    function getOneList() {
		
		let categoryNo = $("input[name=categoryNo]").val();
		let answered = $("select[name=answered]").val();
		let page = $("input[name=page]").val();
		let keyword = $("input[name=keyword]").val();
		
		let $tbody = $(".oneList").empty()
		let $pagination = $(".pagination").empty();
		
		$.getJSON("/admin/support/one/list", {categoryNo:categoryNo, answered:answered, page:page, keyword:keyword}, function(result) {
			
			// 총 건수 업데이트
       		$('#totalCnt').text(result.pagination.totalRows);
       		
       		let oneList = result.oneList;
       		let pagination = result.pagination;
       		
       		if (oneList.length === 0) {
				   $tbody.append(`<tr><th colspan='6' style="text-align:center;">조회된 내역이 없습니다.</th></tr>`);
				   $pagination.empty();
			} else {
				 const tbodyHtml = oneList.map(function(one, index) {
					return `
				<tr>
		            <td>${one.no}</td>
		            <td>${one.theater == null ? '센터' : one.theater.name}</td>
		            <td>${one.category.name}</td>
		            <td style="text-align:left;">
		            	<a class="text-black text-decoration-none"
		            		href="/admin/support/one/detail?no=${one.no}"
				            data-no="${one.no}">
						    ${one.title}
						</a>
		            </td>
		            <td>${one.answered == 'Y' ? '답변완료' : '미답변'}</td>
		            <td>${one.createDate}</td>
	            </tr>
				`	 
				}).join("\n");
				
				$tbody.html(tbodyHtml);
				$pagination.html(renderPagination(pagination));
			 };
		})
    }
    
    $("#table-one tbody").on("click", "a", function(event) {
        event.preventDefault();
        
        let oneNo = $(this).attr("data-no");
        $("#actionForm input[name=no]").val(oneNo);
        $("#actionForm").attr("action", '/admin/support/one/detail');

        document.querySelector("#actionForm").submit();
    })
    
    
});

