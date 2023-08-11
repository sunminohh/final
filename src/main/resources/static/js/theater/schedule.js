$(() => {
	
	let today = dayjs().format("YYYY-MM-DD");
	refreshSchedule(today);

	function refreshSchedule(date) {
		let theaterNo = $("p.name").attr("data-theater-no");
		let $theaterListBox = $(".theater-list-box").empty(); 
		$.getJSON("/schedule/list",{"theaterNo":theaterNo,"date":date},function(data){
			console.log(data);
			data.movieList.forEach(function(movie) {
            	// movie 객체를 반복문으로 처리
            	console.log(movie);
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
            		console.log(screen);
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
            		$theaterListBox.find(".movie-" + movie.movieNo).append(screenContents);
            		screen.scheduleList.forEach(function(schedule) {
            			// schedule 객체를 반복문으로 처리
            			console.log(schedule);
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
            			$theaterListBox.find("tr").append(scheduleContents);
        			});
        		});
        	});
		}).fail(function(jqxhr, textStatus, error) {
    		// 요청이 실패한 경우 처리할 내용
    		var err = textStatus + ", " + error;
    		console.log("Request Failed: " + err);
		});
	}
	
	$(".date-area").on("click","button", function(){
		let date = $(this).attr("date-data");
		console.log(date);
		
		refreshSchedule(date);
		
	 });
	 
	
});
