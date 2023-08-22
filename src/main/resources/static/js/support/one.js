
$(function() {
	
	const params = new URLSearchParams(location.search);
	const defaultKeyword = params.get('keyword');
	if (defaultKeyword) {
		$("input[name=keyword]").val(defaultKeyword);
		refreshList();
	}

	// 탭메뉴
	$('li.tab-link').click(function() {
		$('li.tab-link').removeClass('current');
		$('button.btn').removeClass('current');
		$(this).addClass('current');
		$(this).find('button.btn').addClass('current');
		
		let tab = $(this).find('button').attr("data-tab");
		$("input[name=tab]").val(tab);
		$("input[name=page]").val(1);
		refreshList();
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
		let numericValue = $(this).val().replace(/[^0-9]/g, '');

		// 4자리로 제한
		if (numericValue.length > 4) {
			numericValue = numericValue.slice(0, 4);
		}

		// 입력 필드에 반영
		$(this).val(numericValue);
	});
	
	// 폼 알림창
	$("#btn-submit").on("click", function(event) {
		let checkbox = $('#chk').prop('checked');
		let onesupport = $('#onesupport').prop('checked');
		let onetheater = $('#onetheater').prop('checked');
		let oneCat = $("#oneCat").val();
		let location = $("#location").val();
		let theater = $("#theater").val();
		let guestname = $("input[name='guestName']").val();
		let guestemail = $("input[name='guestEmail']").val();
		let guestPassword = $("input[name='guestPassword']").val();
		let title = $("input[name='title']").val();
        let content = $("#textarea").val();
        
        if (checkbox == false) {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '개인정보 수집에 대한 동의가 필요합니다.'
            });
        } else if (onetheater == true && location === null) {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '지역을 선택 해주세요.'
            });
        } else if (onetheater == true && theater === null) {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '극장을 선택 해주세요.'
            });
		} else if (oneCat === null) {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '문의유형을 선택 해주세요.'
            });
        } else if (guestname === '') {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '이름을 입력 해주세요.'
            });
        } else if (guestemail === '') {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '이메일을 입력 해주세요.'
            });
        } else if (guestPassword === '') {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '비밀번호를 입력 해주세요.'
            });
        } else if (title === '') {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '제목을 입력 해주세요.'
            });
		} else if (content === '') {
	        event.preventDefault(); 
            Swal.fire({
                icon: 'error',
                text: '내용을 입력 해주세요.'
            });
        } else {
            $(".insertform").submit();
        }
    });
    
   

	// 나의 문의내역에서 일대일 문의내역 조회
	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val();

		refreshList();
	});
	
	// 폼 전송 이벤트
	$("#actionForm").on('submit', function(e) {
		e.preventDefault();
		refreshList();
	});

	// 페이저번호클릭했 떄
	$(".pagination").on("click", ".page-number-link", function(event) {
		event.preventDefault();
		let page = $(this).attr("data-page");
		$("input[name=page]").val(page);
		
		
		refreshList(); // 해당 페이지 내용 갱신
	});


	refreshList();
	// 초기 페이지 로드
	function refreshList() {

		let page = $("input[name=page]").val();
		// 답변여부 조회
		let answered = $("select[name=answered]").val()
		// 키워드 조회
		let keyword = $("input[name=keyword]").val();

		// 요청데이터 생성
		const $tbody = $(".oneList ").empty();
		let $pagination = $(".pagination");

		// 요청 타입
		// 1:1 문의: tab-one, 분실물 문의: tab-lost
		const tabType = $(".tab-link .current").data('tab');
		
		 if (tabType === 'tab-one') {
                $('#tabType').text('1:1 문의내역');
                $('.float-r').attr('href', '/support/one').attr('title', '1:1 문의하기').text('1:1 문의하기');
            } else if (tabType === 'tab-lost') {
                $('#tabType').text('분실물 문의내역');
                $('.float-r').attr('href', '/support/lost/form').attr('title', '분실물 문의 등록하기').text('분실물 문의하기');
            }
            
		const requestUrl = tabType === 'tab-one' ? '/support/one/list' : '/support/lost/mylist';

		$.getJSON(requestUrl, {answered:answered, page:page,  keyword:keyword}, function(result) {
			
			
			
			// 총 건수 업데이트
			$('#totalCnt').text(result.pagination.totalRows);

			let list = tabType === 'tab-one' ? result.oneList : result.lostList;
			let pagination = result.pagination;

			if (list.length === 0) {
				$tbody.append(`<tr><th colspan='6' style="text-align:center;">조회된 내역이 없습니다.</th></tr>`);
				$pagination.empty();
			} else {

				const tbodyHtml = list.map(function(board, index) {
					return `
				<tr>
		            <td>${board.no}</td>
		            <td>${board.theater == null ? '센터' : board.theater.name}</td>
		            <td>${tabType === 'tab-one' ? board.category.name : '분실물 문의'}</td>
		            <td style="text-align:left;">
		            	<a href="${tabType == 'tab-one' ? 'myinquery/detail?no=' + encodeURIComponent(board.no) : 'mylost/detail?no=' + encodeURIComponent(board.no)}" class="text-black text-decoration-none"
		            		data-no="${board.no}">
						    ${board.title}
						</a>
		            </td>
		            <td>${board.answered == 'Y' ? '답변완료' : '미답변'}</td>
		            <td>${board.createDate}</td>
	            </tr>
				`
				}).join("\n");

				$tbody.html(tbodyHtml);
				$pagination.html(renderPagination(pagination));
			}
		})
	}
	
	$(".board-list tbody").on("click", "a", function(event) {
		event.preventDefault();
		
		let boardNo = $(this).attr("data-no");
		let tab = $("input[name=tab]").val(); 
		if (tab == 'tab-one') {
			$("#actionForm").attr("action", '/support/one/myinquery/detail?no=' + boardNo);
		} else {
			$("#actionForm").attr("action", '/support/one/mylost/detail?no=' + boardNo);
		}
		
		document.querySelector("#actionForm").submit();
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
	              window.location.href = '/support/one/delete?no=' + no;      
	              
	        } else if (result.dismiss === Swal.DismissReason.cancel) {
	            
	        }
	    });
	});
	
	// 폼 사진첨부
	document.getElementById("uploadBtn").addEventListener("click", function() {
	    document.getElementById("fileInput").click(); // 파일 입력 요소를 클릭
	});
	
	document.getElementById("fileInput").addEventListener("change", function() {
	    let selectedFiles = this.files;
	    let imgList = document.getElementById("imgList");
	    let uploadBtn = document.getElementById("uploadBtn");
	
	    // 이미지 첨부 개수가 5개를 초과하는지 확인
	    if (selectedFiles.length + imgList.children.length > 5) {
	        alert("최대 5개의 이미지만 첨부 가능합니다.");
	        this.value = "";
	        return;
	    }
	
	    // 선택된 파일을 순회하면서 파일명과 삭제 버튼을 #imgList에 추가
	    for (let i = 0; i < selectedFiles.length; i++) {
	        let fileNameElement = document.createElement("p");
	        fileNameElement.textContent = "선택된 파일: " + selectedFiles[i].name;
	
	        let deleteButton = document.createElement("button");
	        deleteButton.textContent = "X";
	        deleteButton.className = "btn-del";
	
	        // 삭제 버튼을 클릭했을 때 해당 파일명과 삭제 버튼을 삭제
	        deleteButton.addEventListener("click", function() {
	            let parentDiv = this.parentElement;
	            parentDiv.remove();
	            // 파일 입력을 초기화하여 추가적인 파일 첨부막기
	            document.getElementById("fileInput").value = "";
	            // 파일 선택 버튼을 활성화
	            uploadBtn.disabled = false;
	        });
	
	        // 파일명과 삭제 버튼을 함께 추가
	        let fileContainer = document.createElement("div");
	        fileContainer.appendChild(fileNameElement);
	        fileContainer.appendChild(deleteButton);
	        imgList.appendChild(fileContainer);
	    }
	
	    // 이미지 첨부 개수가 5개 이상일 경우 파일 선택 버튼을 비활성화
	    if (imgList.children.length >= 5) {
	        uploadBtn.disabled = true;
	    }
	});
	
	
});


