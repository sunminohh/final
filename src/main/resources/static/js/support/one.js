
$(function() {
	
	// 탭메뉴	
	$('li.tab-link.current').click();

    $('li.tab-link').click(function() {
		
	  let tabType = $(this).find('button').attr("data-tab");
	  if (tabType == "tab-one") {
		  refreshOne(1);
	  } else if (tabType == 'tab-lost') {
		  refreshLost(1);
	  }

      $('li.tab-link').removeClass('current');
      $('button.btn').removeClass('current');

      $(this).addClass('current');
      $(this).find('button.btn').addClass('current');
    });
    

	// 폼
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
		
		let $selectLocation = $("#location").empty();
		$selectLocation.append(`<option value="" selected disabled>지역선택</option>`)
		
		$.getJSON("/support/one/getLocation", function(locations) {
			locations.forEach(function(loc) {
				let option = `<option value="${loc.no}"> ${loc.name}</option>`;
				$selectLocation.append(option);
			})
		})
	})
	
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
	
	// 폼 비번 4자리
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
		
		refreshOne();
	});
	
	// 페이저번호클릭했 떄
	  $(".pagination").on("click", ".page-number-link", function(event) {
        event.preventDefault();
        let page = $(this).attr("data-page");
        refreshOne(page); // 해당 페이지 내용 갱신
    });

    // 초기 페이지 로드
    refreshOne(1);
	function refreshOne(page) {
		// 답변여부 조회
		let answered = $("select[name=answered]").val()
		// 키워드 조회
		let keyword = $("input[name=keyword]").val();
		
		// 요청데이터 생성
		let $tbody = $(".oneList ").empty()
		let $pagination = $(".pagination").empty();

		$.getJSON("/support/one/list", {answered:answered, page:page,  keyword:keyword}, function(result) {
			
		// 총 건수 업데이트
		$('#totalCnt').text(result.pagination.totalRows);	
		
		let oneList = result.oneList;
		let pagination = result.pagination;
		
		if (oneList.length === 0) {
			$tbody.append(`
					<tr><th colspan='6' style="text-align:center;">조회된 내역이 없습니다.</th></tr>
					`
					);
		} else {
			oneList.forEach(function(one, index) {
				let content = `
				<tr>
		            <td>${one.no}</td>
		            <td>${one.theater == null ? '센터' : one.theater.name}</td>
		            <td>${one.category.name}</td>
		            <td style="text-align:left;">${one.title}</td>
		            <td>${one.answered == 'Y' ? '답변완료' : '미답변'}</td>
		            <td>${one.createDate}</td>
	            </tr>
				`
				
				$tbody.append(content);
			})
			
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
	
	
	
	// 분실물 문의내역 조회
	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val(1);
		
		refreshLost();	
	})
	
	// 페이지번호 클릭했을 때
	$(".pagination").on("click", ".page-number-link", function(event) {
		event.preventDefault();
		let page = $(this).attr("data-page");
		refreshLost(page);
	})
	
	// 초기 페이지 로드
	function refreshLost(page) {
		let answered = $("select[name=answered]").val();
		let keyword = $("input[name=keyword]").val();
		
		let $tbody = $(".oneList").empty();
		let $pagination = $(".pagination").empty();
		
		$.getJSON("/support/lost/list", {answered:answered, page:page, keyword:keyword}, function(result) {
			
			$("#totalCnt").text(result.pagination.totalRows);	
			
			let lostList = result.lostList;
			let pagination = result.pagination;
			
			if (lostList.length === 0) {
				$tbody.append(`
					<tr><th colspan='6' style="text-align:center;">조회된 내역이 없습니다.</th></tr>
					`
					);
			} else {
				lostList.forEach(function(lost, index) {
				let content = `
				<tr>
		            <td>${lost.no}</td>
		            <td>${lost.theater.name}</td>
		            <td>분실물 문의</td>
		            <td style="text-align:left;">${lost.title}</td>
		            <td>${lost.answered == 'Y' ? '답변완료' : '미답변'}</td>
		            <td>${lost.createDate}</td>
	            </tr>
				`
				
				$tbody.append(content);
			})
			
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
})