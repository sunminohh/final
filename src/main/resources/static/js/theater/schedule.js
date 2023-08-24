$(() => {
	
	let today = dayjs();
	refreshSchedule(today);
	refreshDate();
	
	function refreshSchedule(date) {
		let theaterNo = $("p.name").attr("data-theater-no");
		let $theaterListBox = $(".theater-list-box").empty(); 
		let time = dayjs(date).add(10, 'minute').format("HH:mm");
		date = dayjs(date).format("YYYY-MM-DD");
		let currenttime = dayjs();
		$.getJSON("/schedule/list",{"theaterNo":theaterNo,"date":date,"time":time},function(data){
			data.movieList.forEach(function(movie) {
            	// movie 객체를 반복문으로 처리
            	let movieContents=`
            		<div class="theater-list movie-${movie.movieNo}">
            			<div class="theater-tit">
							<p class="movie-grade age-${movie.contentRating}"></p>
							<p>
								<a href="/movie-detail?rpstMovieNo=${movie.movieNo}" title="${movie.movieTitle} 상세보기">${movie.movieTitle}</a>
							</p>
							<p class="infomation">
								<span>상영중</span>/상영시간 ${movie.runtime}분
							</p>
						</div>
					</div>
            	`
            	$theaterListBox.append(movieContents);
            	movie.screenList.forEach(function(screen) {
           			 // screen 객체를 반복문으로 처리
            		let screenContents =`
            			<div class="theater-type-box screen-${screen.screenId}">
							<div class="theater-type">
								<p class="theater-name">${screen.screenName}</p>
								<p class="chair">총 ${screen.seats}석</p>
							</div>
							<div class="theater-time">
								<div class="theater-type-area">${screen.screenInfo}</div>
								<div class="theater-time-box">
									<table class="time-list-table">
										<caption>상영시간을 보여주는 표 입니다.</caption>
										<colgroup>
											<col style="width: 99px;">
											<col style="width: 99px;">
											<col style="width: 99px;">
											<col style="width: 99px;">
											<col style="width: 99px;">
											<col style="width: 99px;">
											<col style="width: 99px;">
											<col style="width: 99px;">
										</colgroup>
										<tbody>
											<tr>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>	
            		`
            		let $movieBox = $theaterListBox.find(".movie-" + movie.movieNo).append(screenContents);
            		screen.scheduleList.forEach(function(schedule) {
						let scheduleStartTime = dayjs(date+schedule.start);
            			// schedule 객체를 반복문으로 처리
            			if (scheduleStartTime.isAfter(currenttime)) {
	            			let scheduleContents = `
	            				<td class="" theater-no="${data.theaterNo}" play-schdl-no="${schedule.id}"
															rpst-movie-no="${movie.movieNo}" theab-no="${screen.screenId}" play-de="${data.date}"
															play-seq="${schedule.turn}" >
									<div class="td-ab">
										<div class="txt-center">
											<a href="" title="영화예매하기">
												<div class="ico-box">
													<i class="iconset ico-off"></i>
												</div>
												<p class="time">${schedule.start}</p>
												<p class="chair">${schedule.remainingSeats}석</p>
												<div class="play-time">
													<p>${schedule.start}~${schedule.end}</p>
													<p>${schedule.turn}회차</p>
												</div>
											</a>
										</div>
									</div>
								</td>
	            			`
            			$movieBox.find("tr").append(scheduleContents);
	            		}
        			});
        		});
        	});
		}).fail(function(jqxhr, textStatus, error) {
    		// 요청이 실패한 경우 처리할 내용
    		var err = textStatus + ", " + error;
    		console.log("Request Failed: " + err);
		});
	}
	
	$(".date-area").on("click","button:not(.disabled)", function(){
		let date = $(this).attr("date-data");
		$(this).addClass("on");
		$(this).siblings().removeClass("on");
		
		refreshSchedule(date);
		
	 });
	 	// 극장 상세 탭 버튼 클릭시 
	$(".tab-list a").on("click", function() {
		// 다른 버튼은 비활성화
		// 누른 버튼 활성화
		$(this).closest("li").siblings().removeClass("on");
		$(this).closest("li").addClass("on");
		// 누른 탭의 href로 탭 아이디를 가져온다.
		let tabId = $(this).attr("href");
		// 다른 탭 비활성화
		$(tabId).siblings().removeClass("on");
		// 탭 아이디로 탭을 찾아서 활성화
		$(tabId).addClass("on");
		refreshDate();
	})
	 
	function refreshDate() {
		dayjs.locale('ko-kr')
		dayjs.extend(dayjs_plugin_isSameOrAfter)
		let today = dayjs();
		let targetDay = today.add(13, 'day');
		let htmlContents = `<div class="year" style="left: 30px; z-index: 1; opacity: 1;">${today.get("y")}.${today.get("M") + 1}</div>`;
		if (!dayjs(today).isSameOrAfter(targetDay, 'month')) {
			htmlContents += `<div class="year" style="left: 30px; z-index: 1; opacity: 0;">${targetDay.get("y")}.${targetDay.get("M") + 1}</div>`;
		}
		$(".year-area").html(htmlContents);
		htmlContents = ``;
		let currentDay = today.clone();
		for (i = 1; i < 15; i++) {
			let weekDay = currentDay.format('dd');
			if (currentDay.diff(today, 'day') == 0) {
				weekDay = '오늘';
			}
			if (currentDay.diff(today, 'day') == 1) {
				weekDay = '내일';
			}
			let weekdayno = currentDay.get('day')
			htmlContents += `<button class="disabled ${today.isSame(currentDay) ? 'on' : ''} ${weekdayno == 0 ? 'holi' :
				weekdayno == 6 ? 'sat' : ''}" type="button" date-data="${currentDay.format('YYYY-MM-DD')}"
											month="${currentDay.get('month')}">
											<span class="ir">${currentDay.format('YYYY년MM월')}</span><em
												style="pointer-events: none;">${currentDay.get('date')}<span
												style="pointer-events: none;" class="ir">일</span></em><span
												class="day-kr"
												style="pointer-events: none; display: inline-block">${weekDay}</span><span
												class="day-en" style="pointer-events: none; display: none">Tue</span>
										</button>`;
			currentDay = currentDay.add(1, 'day');
		}
		$(".date-area .wrap").html(htmlContents);
		activateButton();
	}
	
	// 날짜버튼 활성화
	function activateButton() {
		let $buttons = $(".date-area button");
		let theaterNo = $("p.name").attr("data-theater-no");

		$.getJSON("/schedule/checkSchedule", { "theaterNo": theaterNo }, function(data) {
			data.dateList.forEach(function(date) {
				$buttons.each(function(index, button) {
					let buttondate = dayjs($(button).attr("date-data"))
					let scheduledate = dayjs(date)
					if (dayjs(scheduledate).isSame(buttondate, 'day')) {
						$(button).removeClass("disabled");
					} 
				})
			})
		})

	}
	
	
});
