$(function() {
 	/// 탭컬러 바꾸기
	$('li.tab-link').click(function() {

		$('li.tab-link').removeClass('current');
		$('button.btn').removeClass('current');

		$(this).addClass('current');
		$(this).find('button.btn').addClass('current');
  
		let categoryNo = $(this).find('button').attr("data-category-no");
		$("input[name=categoryNo]").val(categoryNo);
		$("input[name=page]").val(1);
		
		document.querySelector("#actionForm").submit();
    });
    
	// 엔터쳐서 검색하기 
	const params = new URLSearchParams(location.search);
	const defaultKeyword = params.get('keyword');
	if (defaultKeyword) {
		$("input[name=keyword]").val(defaultKeyword);
	}
    // 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val(1);
		document.querySelector("#actionForm").submit();
	});
	
	// 폼 전송 이벤트
	$("#actionForm").on('submit', function(e) {
		e.preventDefault();
		document.querySelector("#actionForm").submit();
	});
	
	// 페이지번호 클릭했을떄
	$('.pagination').on('click', '.page-number-link', function(event) {
		event.preventDefault();
		let page = $(this).attr("data-page");
		
		// 모든 페이지 번호 링크에서 active 클래스 제거
        $('.page-number-link').removeClass('active');

        // 클릭한 페이지 번호에만 active 클래스 추가
        $(this).addClass('active'); 
		
		$("input[name=page]").val(page);
		
		document.querySelector("#actionForm").submit();
		
	})	
	  

})









