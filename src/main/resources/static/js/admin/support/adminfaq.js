
$(document).ready(function() {
	
	// 폼에서 카테고리 조회
	let $selectCategory = $("#faqCat").empty();
	$selectCategory.append(`<option value="" selected disabled>카테고리 선택</option>`)
	$.getJSON("/admin/support/faq/getCategory?type=faq", function(categories) {
		categories.forEach(function(cat) {
			let option = `<option value="${cat.no}"> ${cat.name}</option>`;
				$selectCategory.append(option);
		})
	})

	const params = new URLSearchParams(location.search);
	const defaultKeyword = params.get('keyword');
	if (defaultKeyword) {
		$("input[name=keyword]").val(defaultKeyword);
		getFaqList();
	}

    $('li.tab-link').click(function() {

	      $('li.tab-link').removeClass('current');
	      $('button.btn').removeClass('current');
	
	      $(this).addClass('current');
	      $(this).find('button.btn').addClass('current');
    });
    // 탭메뉴 끝

	// 검색버튼 클릭했을 때
	$("#btn-search").click(function() {
		let keyword = $("input[name=keyword]").val();
		if (keyword == "") {
			return false;
		}
		$("input[name=page]").val(1);
		
		getFaqList();
	});

	// 폼 전송 이벤트
	$("#actionForm").on('submit', function(e) {
		e.preventDefault();
		getFaqList();
	});

	/* 폼 없을때 사용
	$("input[name='keyword']").on('keyup', function(e) {
		if (e.keyCode === 13) {
			getFaqList();
		}
	});
	*/

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
		
		let keyword = $("input[name=keyword]").val();
		let categoryNo = $("input[name=catNo]").val();
		let page = $("input[name=page]").val();
		
		let $ul = $(".faq-list ul").empty()
		let $pagination = $(".pagination").empty();
		
		$.getJSON("/admin/support/faq/list", {keyword:keyword, catNo:categoryNo, page:page}, function(result) {
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

				$pagination.html(renderPagination(pagination));

			}
		});
		
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
