
$(document).ready(function() {
	
	// 인서트폼에서 카테고리 조회
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

    // 탭컬러 바꾸기
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

	// 폼 전송 이벤트
	$("#actionForm").on('submit', function(e) {
		e.preventDefault();
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
		
		let keyword = $("input[name=keyword]").val();
		let categoryNo = $("input[name=catNo]").val();
		let page = $("input[name=page]").val();
		
		let $tbody = $(".faqList").empty()
		let $pagination = $(".pagination").empty();
		
		$.getJSON("/admin/support/faq/list", {keyword:keyword, catNo:categoryNo, page:page}, function(result) {
			// 총 건수 업데이트
       		$('#totalCnt').text(result.pagination.totalRows);
			
			let faqList = result.faqList;
			let pagination = result.pagination;
			
			if (faqList.length === 0) {
				$tbody.append(`<tr><th colspan='5' style="text-align:center;">조회된 내역이 없습니다.</th></tr>`)
			} else {
				const tbodyhtml = faqList.map(function(faq, index) {
					return `
				<tr>
		            <td>${faq.no}</td>
		            <td>${faq.category.name}</td>
		            <td style="text-align:left;">
						<a class="text-black text-decoration-none"
							 href="/admin/support/faq/detail?no=${faq.no}"
							 data-no="${faq.no}">
			            	 ${faq.title}
		            	</a>
		            </td>
		            <td>${faq.orderNo}</td>
		            <td>${faq.updateDate}</td>
	            </tr>
					`
				}).join("\n");
				
				$tbody.html(tbodyhtml);

				$pagination.html(renderPagination(pagination));
			}
		});
	}
	
	 $("#table-faq tbody").on("click", "a", function(event) {
		event.preventDefault();
		
		let faqNo = $(this).attr("data-no");
		$("#actionForm input[name=no]").val(faqNo);
		
		document.querySelector("#actionForm").submit();
	})
});
