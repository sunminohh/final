$(function() {
	
	const urlParams = new URLSearchParams(window.location.search);
	const locationNo = urlParams.get('locationNo');
	const theaterNo = urlParams.get('theaterNo');
	
	if (locationNo && theaterNo) {
		 selectTheater(locationNo, theaterNo);
	}
	
	
	// 폼에서 지역조회
	$("#theater").prop("disabled", true);
	
	let $selectLocation = $("#location").empty();
	$selectLocation.append(`<option value="" selected>지역선택</option>`)
	
	$.getJSON("/support/lost/getLocation", function(locations) {
		locations.forEach(function(loc) {
			let option = `<option value="${loc.no}" ${locationNo == loc.no ? 'selected' : ''}> ${loc.name}</option>`;
			$selectLocation.append(option);
		})
		
	})
	
	// 극장선택
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
	
	function selectTheater(locationNo, theaterNo) {
		
		let $selectTheater = $("#theater").empty();
		
		$selectTheater.append(`<option value="" selected disabled>극장선택</option>`)
		
		$.getJSON("/support/lost/getTheaterByLocationNo?locationNo="+ locationNo, function(theaters){
			theaters.forEach(function(thr) {
				let option = `<option value="${thr.no}" ${theaterNo == thr.no ? 'selected' : ''}> ${thr.name}</option>`;
				$selectTheater.append(option);
			})
			$("#theater").prop("disabled", false);
		})
	}
	
	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val(1);
		
		getLostList();
	});
	
	const params = new URLSearchParams(location.search);
	const defaultKeyword = params.get('keyword');
	if (defaultKeyword) {
		$("input[name=keyword]").val(defaultKeyword);
		getLostList();
	}
	
	// 폼 전송 이벤트
	$("#actionForm").on('submit', function(e) {
		e.preventDefault();
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
		let $pagination = $(".pagination");
		
		$.getJSON("/admin/support/lost/list", {locationNo:locationNo, theaterNo:theaterNo, answered:answered, page:page,  keyword:keyword}, function(result) {
			
			// 총 건수 업데이트
			$('#totalCnt').text(result.pagination.totalRows);
			
			let lostList = result.lostList;
			let pagination = result.pagination;
			
			if (lostList.length === 0) {
				$tbody.append(`<tr><th colspan='5' style="text-align:center;">조회된 내역이 없습니다.</th></tr>`);
				$pagination.empty();
			} else {
				const tbodyHtml = lostList.map(function(lost, index) {
					return `
						<tr>
						 	<td>${lost.no}</td>
				            <td>${lost.theater.name}</td>
				            <td style="text-align:left;">
				            	<a class="text-black text-decoration-none"
				            		href="/admin/support/lost/detail?no=${lost.no}"
				            		data-no="${lost.no}">
				            		${lost.title }
				            	</a>
				            </td>
				            <td>${lost.answered == 'Y' ? '답변완료' : '미답변'}</td>
				            <td>${lost.createDate}</td>
			           </tr>
						`
				}).join("\n");
				
				$tbody.html(tbodyHtml);
				$pagination.html(renderPagination(pagination));
			}
		})
	}
	
     $("#table-lost tbody").on("click", "a", function(event) {
		event.preventDefault();
		
		let lostNo = $(this).attr("data-no");
		$("#actionForm input[name=no]").val(lostNo);
		
		document.querySelector("#actionForm").submit();
	})
	
	// 삭제 버튼 띄우기
    $("#delete-btn").on("click", function(event) {
	    event.preventDefault();
		let no = $('input[name=no]').val();
		
	    Swal.fire({
	        icon: 'warning',
	        title: '정말 삭제하시겠습니까?',
	        showCancelButton: true,
	        confirmButtonText: '네',
	        cancelButtonText: '아니오',
	    }).then((result) => {
	        if (result.isConfirmed) {
	              window.location.href = '/admin/support/lost/delete?no=' + no;      
	              
	        } else if (result.dismiss === Swal.DismissReason.cancel) {
	            
	        }
	    });
	});
		
})


