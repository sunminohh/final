
// 탭메뉴	
$(document).ready(function() {
	  
    $('li.tab-link.current').click();

    $('li.tab-link').click(function() {

	      $('li.tab-link').removeClass('current');
	      $('button.btn').removeClass('current');
	
	      $(this).addClass('current');
	      $(this).find('button.btn').addClass('current');
    });


	// 리스트 갱신
	$("li.tab-link").click(function() {
		let categoryNo = $(this).find('button').attr("data-category-no");
		let $ul = $(".faq-list ul").empty()
		
		$.getJSON("/support/faq/list", {catNo:categoryNo}, function(faqList) {
			
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
				
			}
		})
	});

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














