$(function() {
	
	$("#theater").prop("disabled", true);
	
	let $selectLocation = $("#location").empty();
	$selectLocation.append(`<option value="" selected disabled>지역선택</option>`)
	
	$.getJSON("/support/lost/getLocation", function(locations) {
		locations.forEach(function(loc) {
			let option = `<option value="${loc.no}"> ${loc.name}</option>`;
			$selectLocation.append(option);
		})
		
	})
	
	$("#location").change(function() {
		$("#theater").prop("disabled", false);
		
		let locationNo = $(this).val();
		let $selectTheater = $("#theater").empty();
		
		$selectTheater.append(`<option value="" selected disabled>극장선택</option>`)
		
		$.getJSON("/support/lost/getTheaterByLocationNo?locationNo="+ locationNo, function(theaters){
			theaters.forEach(function(thr) {
				let option = `<option value="${thr.no}"> ${thr.name}</option>`;
				$selectTheater.append(option);
			})
		})
		
	});
	
	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val(1);
		
		getLostList();
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
		
		getLostList();
	})	
	
	function getLostList() {
		// form의 값 조회
		let locationNo = $("select[name=locationNo]").val();
		let theaterNo = $("select[name=theaterNo]").val();
		let answered = $("select[name=answered]").val();
		let page = $("input[name=page]").val();
		let keyword = $("input[name=keyword]").val();
		
		let $tbody = $(".lostList ").empty()
		let $pagination = $(".pagination").empty();
		
		$.getJSON("/support/lost/list", {locationNo:locationNo, theaterNo:theaterNo, answered:answered, page:page,  keyword:keyword}, function(result) {
			
			// 총 건수 업데이트
			$('#totalCnt').text(result.pagination.totalRows);
			
			let lostList = result.lostList;
			let pagination = result.pagination;
			
			if (lostList.length === 0) {
				$tbody.append(`
					<tr><th colspan='5' style="text-align:center;">조회된 내역이 없습니다.</th></tr>
					`
					);
			} else {
				lostList.forEach(function(lost, index) {
					let content = `
						<tr>
						 	<td>${lost.no}</td>
				            <td>${lost.theater.name}</td>
				            <td style="text-align:left;">
				            	<a class="text-black text-decoration-none"
				            		href="/support/lost/detail?no=${lost.no}">
				            		${lost.title }
				            	</a>
				            </td>
				            <td>${lost.answered == 'Y' ? '답변완료' : '미답변'}</td>
				            <td>${lost.updateDate}</td>
			           </tr>
					`
					$tbody.append(content);
				})
				
				// 페이지네이션 시작
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
	
	// 폼 비번 4자리
	$(document).ready(function() {
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
    });
	// 폼 끝
	
		$("#btn-submit").on("click", function(event) {
        event.preventDefault(); // 폼 제출 방지

        let content = $("#textarea").val();
        
        if (content === '') {
            Swal.fire({
                icon: 'error',
                text: '내용을 입력 해주세요.'
            });
        } else {
            $(".insertform").submit();
        }
    });
		
})









