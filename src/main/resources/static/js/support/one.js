
$(function() {

	// 탭메뉴
	$('li.tab-link').click(function() {
		$('li.tab-link').removeClass('current');
		$('button.btn').removeClass('current');
		$(this).addClass('current');
		$(this).find('button.btn').addClass('current');
		refreshList(1);
	});


	// 폼/문의선택에서 고객센터 문의 선택했을 때
	$("#onesupport").change(function() {
		let $selectCategory = $("#oneCat").empty();
		$selectCategory.append(`<option value="" selected disabled>문의유형 선택</option>`)

		$("#location").prop("disabled", true);
		$("#theater").prop("disabled", true);

		$.getJSON("/support/one/getCategory?type=고객센터문의", function(categories) {
			categories.forEach(function(cat) {
				let option = `<option value="${cat.no}"> ${cat.name}</option>`;
				$selectCategory.append(option);
			})
		})
	}).trigger("change");

	// 폼/문의선택에서 극장별 문의 선택했을 때
	$("#onetheater").change(function() {
		let $selectCategory = $("#oneCat").empty();
		$selectCategory.append(`<option value="" selected disabled>문의유형 선택</option>`)

		$("#location").prop("disabled", false);

		$.getJSON("/support/one/getCategory?type=극장별문의", function(categories) {
			categories.forEach(function(cat) {
				let option = `<option value="${cat.no}"> ${cat.name}</option>`;
				$selectCategory.append(option);
			})
		})
		// 지역 선택
		let $selectLocation = $("#location").empty();
		$selectLocation.append(`<option value="" selected disabled>지역선택</option>`)

		$.getJSON("/support/one/getLocation", function(locations) {
			locations.forEach(function(loc) {
				let option = `<option value="${loc.no}"> ${loc.name}</option>`;
				$selectLocation.append(option);
			})
		})
	})
	// 극장선택
	$("#location").change(function() {
		$("#theater").prop("disabled", false);

		let locationNo = $(this).val();
		let $selectTheater = $("#theater").empty();

		$selectTheater.append(`<option value="" selected disabled>극장선택</option>`)

		$.getJSON("/support/one/getTheaterByLocationNo?locationNo="+ locationNo, function(theaters){
			theaters.forEach(function(thr) {
				let option = `<option value="${thr.no}"> ${thr.name}</option>`;
				$selectTheater.append(option);
			})
		})

	});

	// 폼 비번 숫자 4자리만 입력받기
	$(".pwnew").on("input", function() {
		// 입력값에서 숫자 이외의 문자 제거
		var numericValue = $(this).val().replace(/[^0-9]/g, '');

		// 4자리로 제한
		if (numericValue.length > 4) {
			numericValue = numericValue.slice(0, 4);
		}

		// 입력 필드에 반영
		$(this).val(numericValue);
	});

	// 일대일 문의내역 조회
	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val(1);

		refreshList();
	});

	// 페이저번호클릭했 떄
	$(".pagination").on("click", ".page-number-link", function(event) {
		event.preventDefault();
		let page = $(this).attr("data-page");
		refreshList(page); // 해당 페이지 내용 갱신
	});

	// 초기 페이지 로드
	refreshList(1);
	function refreshList(page) {

		// 답변여부 조회
		let answered = $("select[name=answered]").val()
		// 키워드 조회
		let keyword = $("input[name=keyword]").val();

		// 요청데이터 생성
		const $tbody = $(".oneList ").empty();
		let $pagination = $(".pagination").empty();

		// 요청 타입
		// 1:1 문의: tab-one, 분실물 문의: tab-lost
		const tabType = $(".tab-link .current").data('tab');
		
		 if (tabType === 'tab-one') {
                $('#tabType').text('1:1 문의내역');
            } else if (tabType === 'tab-lost') {
                $('#tabType').text('분실물 문의내역');
            }
            
		const requestUrl = tabType === 'tab-one' ? '/support/one/list' : '/support/lost/mylist';

		$.getJSON(requestUrl, {answered:answered, page:page,  keyword:keyword}, function(result) {
			
			
			
			// 총 건수 업데이트
			$('#totalCnt').text(result.pagination.totalRows);

			let list = tabType === 'tab-one' ? result.oneList : result.lostList;
			let pagination = result.pagination;

			if (list.length === 0) {
				$tbody.append(`<tr><th colspan='6' style="text-align:center;">조회된 내역이 없습니다.</th></tr>`);
			} else {

				const tbodyHtml = list.map(function(board, index) {
					return `
				<tr>
		            <td>${board.no}</td>
		            <td>${board.theater == null ? '센터' : board.theater.name}</td>
		            <td>${tabType === 'tab-one' ? board.category.name : '분실물 문의'}</td>
		            <td style="text-align:left;">
		            	<a class="text-black text-decoration-none"
		            		>
			            	${board.title}
		            	</a>
		            </td>
		            <td>${board.answered == 'Y' ? '답변완료' : '미답변'}</td>
		            <td>${board.createDate}</td>
	            </tr>
				`
				}).join("\n");

				$tbody.html(tbodyHtml);

				// 페이지네이션
				let firstContent = `
		            <li class="page-item">
		                <a title="첫번째 페이지 보기"
		                   href="list?page=1"
		                   class="page-link page-number-link control first"
		                   data-page="1">1</a>
		            </li>
		        `;
				$pagination.append(firstContent);

				if (result.pagination.currentBlock > 1) {
					let prePage = (pagination.currentBlock -1)* 10
					let nextContent = `
		                <li class="page-item">
		                    <a title="이전 10페이지 보기"
		                       href="list?page=${prePage}"
		                       class="page-link page-number-link control prev"
		                       data-page="${prePage}">${prePage}</a>
		                </li>
		            `;
					$pagination.append(nextContent);
				}


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


				if (result.pagination.currentBlock < result.pagination.totalBlocks) {
					let nextpage = (pagination.currentBlock)* 10 + 1
					let nextContent = `
		                <li class="page-item">
		                    <a title="이후 10페이지 보기"
		                       href="list?page=${nextpage}"
		                       class="page-link page-number-link control next"
		                       data-page="${nextpage}">${nextpage}</a>
		                </li>
		            `;
					$pagination.append(nextContent);
				}

				let lastContent = `
		            <li class="page-item">
		                <a title="마지막 페이지 보기"
		                   href="list?page=${pagination.totalPages}"
		                   class="page-link page-number-link control last"
		                   data-page="${pagination.totalPages}">${pagination.totalPages}</a>
		            </li>
		        `;
				$pagination.append(lastContent);

			}
		})
	}
});