$(() => {
	// 극장 상세 탭 버튼 클릭시 
    $(".tab-list a").on("click", function(){
		// 다른 버튼은 비활성화
		// 누른 버튼 활성화
		$(this).closest("li").siblings().removeClass("on");
		$(this).closest("li").addClass("on");
		// 누른 탭의 href로 탭 아이디를 가져온다.
		let tabId = $(this).attr("href");
		// 다른 탭 비활성화
		$(tabId).siblings().removeClass("on");
		// 탭 아이디로 탭을 찾아서 활성화
		$(tabId).addClass("on");
	})
	
	$(".theater-place button").on("click", function(){
		$(this).closest("li").addClass("on");
		$(this).closest("li").siblings().removeClass("on");
	})
});

