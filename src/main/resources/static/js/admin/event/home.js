
$(document).ready(function() {
    // 이전에 클릭한 카테고리 정보를 로컬 스토리지에서 가져와서 적용

	// 탭을 클릭
    $(".tab-list ul li").on("click", function() {
        // 기존에 추가된 'on' 클래스를 모두 제거합니다.
        $(".tab-list ul li").removeClass("on");

        // 클릭한 카테고리의 li 태그에 'on' 클래스를 추가합니다.
        $(this).addClass("on");

       let catNo = $(this).attr("data-cat-no");
       $("#actionForm input[name=catNo]").val(catNo);
       $("#actionForm input[name=page]").val(1);
       $("#actionForm input[name=keyword]").val();
       $("#actionForm select[name=status]").val();
       
       $("#actionForm").trigger("submit");
    });
    
    // 검색 버튼을 큭릭
    $("#actionForm #searchBtn").click(function() {
		$("#actionForm input[name=page]").val(1);
		
		$("#actionForm").trigger("submit");
	});
    // 진행여부를 변경
    $("#actionForm select[name=status]").change(function() {
		$("#actionForm input[name=page]").val(1);
		
		$("#actionForm").trigger("submit");
	});
	
	// 페이지번호를 클릭
	//<a href="/admin/event/list"  data-page-no="2">
	$(".pagination a").click(function(event) {
		event.preventDefault();
		let page = $(this).attr("data-page");
		$("#actionForm input[name=page]").val(page);
		
		$("#actionForm").trigger("submit");
	});

});