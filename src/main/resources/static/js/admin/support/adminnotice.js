$(function() {
	
	// 탭컬러 바꾸기
	$('li.tab-link').click(function() {

	      $('li.tab-link').removeClass('current');
	      $('button.btn').removeClass('current');
	
	      $(this).addClass('current');
	      $(this).find('button.btn').addClass('current');
    });
    
    const urlParams = new URLSearchParams(window.location.search);
	const locationNo = urlParams.get('locationNo');
	const theaterNo = urlParams.get('theaterNo');
	
	if (locationNo && theaterNo) {
		 selectTheater(locationNo, theaterNo);
	}
	
	// 지역조회
	$("#theater").prop("disabled", true);
	
	let $selectLocation = $("#location").empty();
	$selectLocation.append(`<option value="" selected>지역선택</option>`)
	
	$.getJSON("/support/lost/getLocation", function(locations) {
		locations.forEach(function(loc) {
			let option = `<option value="${loc.no}" ${locationNo == loc.no ? 'selected' : ''}> ${loc.name}</option>`;
			$selectLocation.append(option);
		})
	})
	
	// 극장조회
	$("#location").change(function() {
		$("#theater").prop("disabled", false);
		
		let locationNo = $(this).val();
		let $selectTheater = $("#theater").empty();
		
		$selectTheater.append(`<option value="" selected disabled>극장선택</option>`)
		
		$.getJSON("/support/lost/getTheaterByLocationNo?locationNo="+ locationNo, function(theaters){
			theaters.forEach(function(thr) {
				let option = `<option value="${thr.no}" ${theaterNo == thr.no ? 'selected' : ''}> ${thr.name}</option>`;
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
	
	// 등록폼 알림
	$("#btn-submit").on("click", function(event) {
		let noticethr = $('#noticethr').prop('checked');
		let location = $('#location').val();
		let theater = $('#theater').val();
		let noticeCat = $('#noticeCat').val();
		let title = $('input[name=title]').val();
		let content = $('textarea[name=content]').val();
		
		if (noticethr == true && location === '') {
			event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '지역을 선택 해주세요.'
            });
        } else if (noticethr == true && theater === null) {
			event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '극장을 선택 해주세요.'
         	 });
         } else if (noticeCat === null) {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '유형을 선택 해주세요.'
         	 });
		 } else if (title === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '제목을 입력 해주세요.'
         	 });
		 } else if (content === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '내용을 입력 해주세요.'
         	 });
		 } else {
			 if (content.length > 10485760) { // 1048576 바이트 = 1MB 
	            Swal.fire({
	                icon: 'warning',
	                text: '첨부파일의 크기가 너무 큽니다. 최대 허용 크기: 10MB',
	            });
	        } else {
				event.preventDefault(); 
	            Swal.fire({
	                icon: 'warning',
	                title: '게시글을 등록 하시겠습니까?',
	                showCancelButton: true,
	                confirmButtonText: '네',
	                cancelButtonText: '아니오',
	            }).then((result) => {
		                if (result.isConfirmed) {
		                    $("#insertform").submit();
		                } else if (result.dismiss === Swal.DismissReason.cancel) {
		
		                }
		            });
	           }
		 }
        
	})
	
	// 수정폼에서 MGV공지가 checked일때
	$("#mgvNotice").change(function() {
		$("#modifyloc").prop("disabled", true);
		$("#modifythr").prop("disabled", true);
	})
	// 수정폼에서 지점공지가 checked일때
	$("#thrNotice").change(function() {
		$("#modifyloc").prop("disabled", false);
		$("#modifythr").prop("disabled", false);
	})
	
	// 수정폼에서 극장조회
	$("#modifyloc").change(function() {
		
		let locationNo = $(this).val();
		let $selectTheater = $("#modifythr").empty();
		
		$.getJSON("/support/lost/getTheaterByLocationNo?locationNo="+ locationNo, function(theaters){
			theaters.forEach(function(thr) {
				let option = `<option value="${thr.no}"> ${thr.name}</option>`;
				$selectTheater.append(option);
			})
		})
		
	});
	
	// 수정폼 알림창 띄우기
	$("#modify-btn-submit").on("click", function(event) {
	
		let noticethr = $('#noticethr').prop('checked');
		let location = $('#location').val();
		let theater = $('#theater').val();
		let noticeCat = $('#noticeCat').val();
		let title = $('input[name=title]').val();
		let content = $('textarea[name=content]').val();
		
		if (noticethr == true && location === '') {
			event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '지역을 선택 해주세요.'
            });
        } else if (noticethr == true && theater === null) {
			event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '극장을 선택 해주세요.'
         	 });
         } else if (noticeCat === null) {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '유형을 선택 해주세요.'
         	 });
		 } else if (title === '') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '제목을 입력 해주세요.'
         	 });
		 } else if (content === '' || content === '<p><br></p>') {
			 event.preventDefault();
			 Swal.fire({
                icon: 'warning',
                text: '내용을 입력 해주세요.'
         	 });
		 } else {
			 if (content.length > 10485760) { // 1048576 바이트 = 1MB 
	            Swal.fire({
	                icon: 'warning',
	                text: '첨부파일의 크기가 너무 큽니다. 최대 허용 크기: 10MB',
	            });
	        } else {
				event.preventDefault(); 
	            Swal.fire({
	                icon: 'warning',
	                title: '게시글을 수정 하시겠습니까?',
	                showCancelButton: true,
	                confirmButtonText: '네',
	                cancelButtonText: '아니오',
	            }).then((result) => {
		                if (result.isConfirmed) {
		                    $("#modifyform").submit();
		                } else if (result.dismiss === Swal.DismissReason.cancel) {
		
		                }
		            });
	           }
		 }
		
	})
	
	// 삭제 버튼 띄우기
    $("#delete-btn").on("click", function(event) {
	    event.preventDefault();
		let no = $('[name=no]').val();
		
	    Swal.fire({
	        icon: 'warning',
	        title: '정말 삭제하시겠습니까?',
	        showCancelButton: true,
	        confirmButtonText: '네',
	        cancelButtonText: '아니오',
	    }).then((result) => {
	        if (result.isConfirmed) {
	              window.location.href = '/admin/support/notice/delete?no=' + no;      
	              
	        } else if (result.dismiss === Swal.DismissReason.cancel) {
	            
	        }
	    });
	});
	
	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val(1);
		
		getNoticeList();
	});
	
	const params = new URLSearchParams(location.search);
	const defaultKeyword = params.get('keyword');
	if (defaultKeyword) {
		$("input[name=keyword]").val(defaultKeyword);
		getNoticeList();
	}
	// 폼 전송 이벤트
	$("#actionForm").on('submit', function(e) {
		e.preventDefault();
		getNoticeList();
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
		
		getNoticeList();
	})	
	  
	// 탭을 클릭했을 때
    $("li.tab-link").click(function() {
	     let categoryNo = $(this).find('button').attr("data-category-no");
			$("input[name=catNo]").val(categoryNo);
			$("input[name=page]").val(1);
			
			getNoticeList();
    });
    
    function getNoticeList() {
		
		let categoryNo = $("input[name=catNo]").val();
		let page = $("input[name=page]").val();
		let locationNo = $("select[name=locationNo]").val();
		let theaterNo = $("select[name=theaterNo]").val();
		let keyword = $("input[name=keyword]").val();
		
		let $tbody = $(".noticeList").empty()
		let $pagination = $(".pagination").empty();
		
		$.getJSON("/admin/support/notice/list", {catNo:categoryNo, locationNo:locationNo, theaterNo:theaterNo,page:page,  keyword:keyword}, function(result) {
			
			// 총 건수 업데이트
       		$('#totalCnt').text(result.pagination.totalRows);
       		
       		let noticeList = result.noticeList;
       		let pagination = result.pagination;
       		
       		if (noticeList.length === 0) {
				   $tbody.append(`<tr><th colspan='5' style="text-align:center;">조회된 내역이 없습니다.</th></tr>`);
				   $pagination.empty();
			} else {
				 const tbodyHtml = noticeList.map(function(notice, index) {
					return `
							<tr>
				                <td>${notice.no}</td>
				                <td>${notice.theater == null || notice.theater.name == null ? 'MGV' : notice.theater.name}</td>
				                <td>${notice.type == '공지' ? '공지' : '이벤트'}</td>
				                <td style="text-align:left;">
							            	<a class="text-black text-decoration-none
	                							d-inline-block text-truncate" style="max-width: 400px;"
							            		href="/admin/support/notice/detail?no=${notice.no}"
							            		data-no="${notice.no}">
							            		${notice.title}
							            	</a>
							            </td>
				                <td>${notice.updateDate}</td>
				            </tr>
							`	 
				}).join("\n");
				
				$tbody.html(tbodyHtml);
				$pagination.html(renderPagination(pagination));
			 };
		})
    }
    
    $("#table-notice tbody").on("click", "a", function(event) {
		event.preventDefault();
		
		let noticeNo = $(this).attr("data-no");
		$("#actionForm input[name=no]").val(noticeNo);
		
		document.querySelector("#actionForm").submit();
	})
    
    
});



