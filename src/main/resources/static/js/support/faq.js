
// 탭메뉴	
$(document).ready(function() {
	  
    $('li.tab-link.current').click();

    $('li.tab-link').click(function() {

	      $('li.tab-link').removeClass('current');
	      $('button.btn').removeClass('current');
	
	      $(this).addClass('current');
	      $(this).find('button.btn').addClass('current');
    });

	// 검색버튼 클릭했을 때
	$("#btn-search").click(function() {
		let keyword = $("input[name=keyword]").val();
		if (keyword == "") {
			return false;
		}
		$("input[name=page]").val(1);
		
		getFaqList();
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
		
		getFaqList();
	})	

	// 탭을 클릭했을 때
	$("li.tab-link").click(function() {
		let categoryNo = $(this).find('button').attr("data-category-no");
		$("input[name=catNo]").val(categoryNo);
		$("input[name=page]").val(1);
		
		 getFaqList();
	});
	
	function getFaqList() {
		// form의 값을 조회한다.
		
		let keyword = $("input[name=keyword]").val();
		let categoryNo = $("input[name=catNo]").val();
		let page = $("input[name=page]").val();
		
		let $ul = $(".faq-list ul").empty()
		let $pagination = $(".pagination").empty();
		
		$.getJSON("/support/faq/list", {keyword:keyword, catNo:categoryNo, page:page}, function(result) {
			/*
				result = {
					faqList: [{}, {}, {}],
					pagination: {page:1, beginPage:1 endPage:2}
				}
			*/
			
			// 총 건수 업데이트
       		$('#totalCnt').text(result.pagination.totalRows);
			
			let faqList = result.faqList;
			let pagination = result.pagination;
			
			if (faqList.length === 0) {
				$ul.append('<li class="no-results">조회된 내역이 없습니다.</li>');
			} else {
				faqList.forEach(function(faq, index) {
					let content = `
						<li>
							<div class="qut">
		    					<a href="#">
		    						<p class="tit">
		    							<span class="font-green">[${faq.category.name}]</span>
		    						</p>
		    						<p class="txt">
		    							<span class="font-green">
		    							</span>
		    							<span>
		    								${faq.title}
		    							</span>
		    						</p>
		    					</a>
		    				</div>
		    				<div class="awn">
		    					<p class="cont">
		    						<span style="font-size:10.0pt">
		    							<span style="line-height:107%">
		    								<span>
		    									${faq.content}
											</span>
										</span>
		    						</span>
		    					</p>
		    				</div>
						</li>`
						
						$ul.append(content);
				})
				$('.faq-list .qut:first').addClass('on');
	    		$('.faq-list .awn:first').show();
				
				
				for (let i = pagination.beginPage; i <= pagination.endPage; i++) {
					let content = `
						 <li class="page-item">
                        	<a href="list?page=${i}" 
                          	 	class="page-link page-number-link ${i == pagination.page ? 'active' : ''}"
                           		data-page="${i}">${i}</a>
                   		 </li>
					`;
					$pagination.append(content);	
				}
			

			}
		})
		
	}
	


	// 아코디언 열고닫기
    // 첫 번째 아코디언 열기
    $('.faq-list .qut:first').addClass('on');
    $('.faq-list .awn:first').show();

    // 아코디언 헤더(.qut)를 클릭했을 때
    $('.faq-list ').on('click', '.qut', function() {
        if (!$(this).hasClass('on')) {
            // 모든 아코디언 닫기
            $('.faq-list .qut').removeClass('on');
            $('.faq-list .awn').slideUp();

            // 현재 아코디언 열기
            $(this).addClass('on');
            $(this).next('.awn').slideDown();
        }
    });
});












