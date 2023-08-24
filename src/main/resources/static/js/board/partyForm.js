
$(function() {
      // 날짜 버튼 만들기
      let today = new Date();

      // 일주일 간의 날짜와 요일을 배열에 저장
      let weekDatesAndDays = [];
      for (let i = 0; i < 7; i++) {
        let date = new Date(today.getTime() + (i * 24 * 60 * 60 * 1000));
        let dateNumber = date.getDate();
        let dayName = date.toLocaleDateString('kr-KR', { weekday: 'short' });

        weekDatesAndDays.push({
          Date : date,
          date: dateNumber,
          day: dayName
        });
      }

      // 결과 출력
      $.each(weekDatesAndDays, function(index, item) {
        let dateSpanId = "#span-date-" + (index + 1);
        let daySpanId = "#span-day-" + (index + 1);
        let date = "#btn-date-" + (index + 1);
		
        $(dateSpanId).text(item.date);
        $(daySpanId).text(item.day);
        
        let year = item.Date.getFullYear();
		let month = ("0" + (item.Date.getMonth() + 1)).slice(-2);
		let day = ("0" + item.Date.getDate()).slice(-2);
		
		let formattedDate = year + "-" + month + "-" + day ;
        
        $(date).attr("th:value", formattedDate)
        
          if (index === 0) {
		    $("#today-date").text(formattedDate);
		  }
      });

	// #date-select button 클릭시 css 조정 및 selectedDate input의 value로 정의
	let selectedDate;
	$("#date-select button").click(function() {
		
		$(this).css('border-bottom-color', 'white').css('color', '#A06E8D')
		$(this).parent().siblings().find('button').css('border-bottom-color', 'rgb(255, 244, 244)').css('color', 'black')
		
		let today = new Date();
		
		const selectedDate = $(this).attr("th:value");
		const movieNo =  $("input[name=movieNo]");
		$("input[name=date]").attr('th:value', selectedDate);

	});
	
	// 영화검새&선택 입력창
	 $(".movie-search-input").on("input", function () {
	    const inputText = $(this).val();
	    const searchItems = $(".movie-search-item");
	
	    if (inputText.length > 0) {
	      $(".movie-search-list").show();
	      searchItems.each(function () {
	        const itemText = $(this).text().toLowerCase();
	        if (itemText.includes(inputText)) {
	          $(this).show();
	        } else {
	          $(this).hide();
	        }
	      });
	    } else {
	      $(".movie-search-list").hide();
	      searchItems.hide();
	    }
	  });
	
	  $(".movie-search-item").on("click", function () {
	    const selectedItem = $(this).text();
	    $(".movie-search-input").val(selectedItem);
	    $(".movie-search-list").hide();
	  });
	
	  $(document).on("click", function (e) {
	    if (!$(e.target).closest(".movie-search-select").length) {
	      $(".movie-search-list").hide();
	    }
	  });

		// 선택 버튼 클릭시 모달을 열고 선택된 영화제목을 모달의 title로 한다. 
	  $(".movie-search-button").on("click", function () {
        let inputText = $(".movie-search-input").val();
        
        if (inputText === "") { // 입력창 내용이 비었을 때
            Swal.fire({
                icon: 'error',
                text: '검색하실 내용을 입력해 주세요.',
            });
        } else {
            let selectedValue = -1; // 기본값을 -1로 설정
            let selectedTitle = "";
            let found = false; // 영화 제목을 찾았는지 여부를 나타내는 변수

            $(".movie-search-list .movie-search-item").each(function () {
                if ($(this).text() === inputText) {
                    selectedValue = parseInt($(this).attr("value"));
                    selectedTitle = $(this).text();
                    found = true; // 영화 제목을 찾았음을 표시
                    return false; // 반복문 종료
                }
            });

            if (!found) { // 리스트에 없는 영화 제목일 경우
                Swal.fire({
                    icon: 'error',
                    text: '영화제목을 입력해주세요.',
                });
            } else { // 리스트에 있는 영화 제목일 경우
                const movieNo = $("input[name=movieNo]");
                movieNo.attr('value', selectedValue);
                
                $(".modal-title").text(selectedTitle);
                // 모달 열기
                openModal(); // 모달을 열기 위한 사용자 정의 함수 호출
            }
        }
    });

    // 모달 열기 함수 정의
    function openModal() {
        const modal = new bootstrap.Modal(document.getElementById('modal-select-movie'));
        modal.show();
    }

	// 영화선택 모달 안에서의 정보 출력
	$("#btn-schedule").on("click", "#movie-select-btn", function(){
	    const start = $(this).find("input[name=start]").val();
	    const end = $(this).find("input[name=end]").val();
	    const theaterName = $(this).find("input[name=theaterName]").val();
	    const screen = $(this).find("input[name=screen]").val();
	    const schedule = $(this).find("input[name=schedule]").val();
	    $("#input-group input[name=scheduleId]").attr('value', schedule);
	    
		let htmlContent = `
						<div class="d-flex justify-content-end">
                    		<p class="me-1"><strong>${theaterName}</strong></p>
                    		<p class="me-1"><strong class="mt-3">${start}~${end}</strong></p>
                    		<p class="me-1"><strong class="mt-3">${screen}</strong></p>
                    	</div>
		`
		$(".seleted-movie-here").html(htmlContent)
	});

	// 모달 선택완료 버튼 -> 성별 셀렉트박스 옆에 영화 정보 표시
	$("#select-complete").on("click", function(){
			const scheduleId = $("input[name=scheduleId]").val();
			$.ajax({
	        url: '/board/party/selectedInfoBySId',
	        method: 'GET',
	        data: {
	            scheduleId: scheduleId
	        },
	        success: function(schedule) {
					
				let htmlContent = `	<div class="selected-movie-info-box pe-2 pt-2 border" style="border-radius: 7px; border-color:rgb(254, 243, 220); background-color: rgb(254, 243, 220); height: 36px;">
					           			<p class="ms-2" style="color:rgb(64, 44, 27); font-size: 13px;">${schedule.movie.title} MGV${schedule.theater.name} ${schedule.date} ${schedule.start} ${schedule.screen.name}</p>
					           		</div>`
				$("#info-here").empty().append(htmlContent);
				
				 $("#modal-select-movie").modal("hide");
	        },
	        error: function(xhr, status, error) {
	        }
	    });
		
	})
	
	$("#btn-submit").on("click", function () {
	    let movieTitle = $("#info-here").find('.selected-movie-info-box p').text();
	    let title = $("input[name='name']").val();
	    let content = $("#summernote").val();
	    let selectedHeadCount = $("#select-headCount").val();
	    let selectedGender = $("#select-gender").val();
	
	    if (movieTitle === '') {
	        Swal.fire({
	            icon: 'error',
	            text: '영화를 선택해주세요.',
	        });
	    } else if (title === '') {
	        Swal.fire({
	            icon: 'error',
	            text: '게시글의 제목을 입력해주세요.',
	        });
	    } else if (content === '') {
	        Swal.fire({
	            icon: 'error',
	            text: '게시글의 내용을 입력해주세요.',
	        });
	    } else if (selectedHeadCount === null) {
	        Swal.fire({
	            icon: 'error',
	            text: '인원수를 선택해주세요.',
	        });
	    } else if (selectedGender === null) {
	        Swal.fire({
	            icon: 'error',
	            text: '성별을 선택해주세요.',
	        });
	    } else if (content.length > 1048576) {
	        Swal.fire({
	            icon: 'error',
	            text: '내용이 너무 큽니다. 최대 허용 크기: 1MB',
	        });
	    } else {
	        Swal.fire({
	            icon: 'warning',
	            title: '게시글을 등록 하시겠습니까?',
	            showCancelButton: true,
	            confirmButtonText: '네',
	            cancelButtonText: '아니오',
	        }).then((result) => {
	            if (result.isConfirmed) {
	                $(".board-insert-form").submit();
	            } else if (result.dismiss === Swal.DismissReason.cancel) {
	                // 아무 작업도 하지 않음
	            }
	        });
	    }
	});
	
	
})
	
	 // 지역 멀티셀렉트
	 	function changeLoc() {
		let el = document.querySelector("select[name=theaterNo]");
		el.innerHTML = "";  // select박스의 내부 컨텐츠를 전부 지운다.
		el.disabled =false; // select박스를 활성화상태로 바꾼다.
		let locationNo = document.querySelector("select[name=locationNo]").value
		let xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200){
				let text = xhr.responseText;
				let theaters = JSON.parse(text);
				if (theaters.length === 0){
					el.disabled = true;
				}else {
					let options = `<option id="theater-option" value="0" selected disabled>극장선택</option>`;
					theaters.forEach(function(theater) {
						options += `<option id="theater-option" value="${theater.no}" >${theater.name}</option>`;

					});
					el.innerHTML = options;
					}
				}
			}
			xhr.open("GET", "theaterByLocationNo?locationNo=" + locationNo );
			xhr.send();
		}
	
	// 날짜, 지역을 선택후 최종적으로 극장 선택시 영화 정보 출력
	function changeTh() {
	    const date = $("input[name=date]").attr('th:value');
	    const movieNo = $("input[name=movieNo]").val();
	    const theaterNo = document.querySelector("select[name=theaterNo]").value
	    $("input[name=theaterNo]").attr('th:value', theaterNo);
	
	    $.ajax({
	        url: '/board/party/scheduleBydateAndMNoAndTno',
	        method: 'GET',
	        data: {
	            date: date,
	            movieNo: movieNo,
	            theaterNo: theaterNo
	        },
	        success: function(schedules) {
	            const htmlContents = [];
	            $("#btn-schedule").html(""); // 버튼 목록 내부 컨텐츠를 전부 지운다.
	            $("#theater-name-here .theater-name span").html("");
	            if (schedules.length === 0) {
	                htmlContents.push(`<div class="ps-5 pb-3 text-center" id="no-answer" >
	                                    <span class="fw-bold ps-5 ms-5">검색 결과가 없습니다.</span> <br>
	                                    <span class="ps-5 ms-5">옵션을 재선택 하시거나 초기화 해보시기 바랍니다.</span>
	                                   </div>`);
	                $("#btn-schedule #no-answer").css("left", "50px");
	            } else {
	                schedules.forEach(function (schedule) {
						const theaterName = schedule.theater.name;
						$("#theater-name-here .theater-name span").text(theaterName);
						$("#theater-name-here .theater-name span").css('color', '#329eb1');
	                    htmlContents.push(`
	
	                        <div class="movie-info ms-3 pt-2 d-flex justify-content-start ">
	                            <button id="movie-select-btn" class="border rounded me-3" style="border-color:rgb(255, 244, 244); width: 120px; height: 60px; background-color: white">
	                                <div class="me-4" id="movie-time" style="border-bottom-color:rgb(255, 244, 244);">
	                                    <p><strong>${schedule.start}</strong> ~ ${schedule.end}</p>
	                                    <p class="pe-5 me-1 mt-1">${schedule.screen.name}</p>
	                                </div>
	                                
	                                <input type="hidden" name="start" value="${schedule.start}">
	                                <input type="hidden" name="end" value="${schedule.end}">
	                                <input type="hidden" name="theaterName" value="${schedule.theater.name}">
	                                <input type="hidden" name="screen" value="${schedule.screen.name}">
	                                <input type="hidden" name="movieTitle" value="${schedule.movie.title}">
	                                <input type="hidden" name="schedule" value="${schedule.id}">
	                            </button>
	                        </div>
	                    `);
	
	                });
	            }
	            const htmlContent = htmlContents.join('');
	            $("#btn-schedule").html(htmlContent);
	        },
	        error: function(xhr, status, error) {
	        }
	    });
	}

		
		
	