$(function() {
	
	const params = new URLSearchParams(location.search);
	const defaultKeyword = params.get('keyword');
	if (defaultKeyword) {
		$("input[name=keyword]").val(defaultKeyword);
		getLostList();
	}
	
	// 폼에서 지역조회
	let $selectLocation = $("#location").empty();
	$selectLocation.append(`<option value="" selected disabled>지역선택</option>`)
	
	$.getJSON("/support/lost/getLocation", function(locations) {
		locations.forEach(function(loc) {
			let option = `<option value="${loc.no}"> ${loc.name}</option>`;
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
	
	// 폼 비번 4자리
	$(document).ready(function() {
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
    });
	
	// 폼 알림창
	$("#btn-submit").on("click", function(event) {
		let checkbox = $('#chk').prop('checked');
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
		} else if (location === null) {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '지역을 선택 해주세요.'
            });
        } else if (theater === null) {
			event.preventDefault();
			 Swal.fire({
                icon: 'error',
                text: '극장을 선택 해주세요.'
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
	              window.location.href = 'delete?no=' + no;      
	              
	        } else if (result.dismiss === Swal.DismissReason.cancel) {
	            
	        }
	    });
	});
	
	// 검색버튼 클릭했을 때
	$("#searchBtn").click(function() {
		$("input[name=page]").val(1);
		
		getLostList();
	});
	
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
		
		$.getJSON("/support/lost/list", {locationNo:locationNo, theaterNo:theaterNo, answered:answered, page:page,  keyword:keyword}, function(result) {
			
			// 총 건수 업데이트
			$('#totalCnt').text(result.pagination.totalRows);
			
			let lostList = result.lostList;
			let pagination = result.pagination;
			
			if (lostList.length === 0) {
				$pagination.empty();
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
				            		href="/support/lost/detail?no=${lost.no}"
				            		data-no="${lost.no}">
				            		${lost.title }
				            	</a>
				            </td>
				            <td>${lost.answered == 'Y' ? '답변완료' : '미답변'}</td>
				            <td>${lost.updateDate}</td>
			           </tr>
					`
					$tbody.append(content);
				});

				$pagination.html(renderPagination(pagination));
			}
		})
	}
	
	
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



	
	
     $("#table-lost tbody").on("click", "a", function(event) {
		event.preventDefault();
		
		let lostNo = $(this).attr("data-no");
		$("#actionForm input[name=no]").val(lostNo);
		$("#actionForm").attr("action", 'lost/detail?no=' + lostNo);
		
		document.querySelector("#actionForm").submit();
	})
		
})









