$(() => {
	function refreshDate(){
		dayjs.locale('ko-kr')
		let today = dayjs();
		let targetDay = today.add(13, 'day');
		let htmlContents = `<div class="year" style="left: 30px; z-index: 1; opacity: 1;">${today.get("y")}.${today.get("M")+1}</div>`;
		dayjs.extend(dayjs_plugin_isSameOrAfter)
		if(!dayjs(today).isSameOrAfter(targetDay, 'month')){
			htmlContents += `<div class="year" style="left: 30px; z-index: 1; opacity: 0;">${targetDay.get("y")}.${targetDay.get("M")+1}</div>`;
		}
		$(".year-area").html(htmlContents);
		htmlContents=``;
		let currentDay = today.clone();
		for(i =1; i < 15 ;i++){
			let weekDay = currentDay.format('dd');
			if(currentDay.diff(today, 'day')==0){
				weekDay = '오늘';
			}
			if(currentDay.diff(today, 'day')==1){
				weekDay = '내일';
			}
			let weekdayno = currentDay.get('day')
			htmlContents += `<button class="disabled ${weekdayno == 0 ? 'holi': 
														weekdayno == 6 ? 'sat':''}" type="button" date-data="${currentDay.format('YYYY.MM.DD')}"
											month="7">
											<span class="ir">${currentDay.format('YYYY년MM월')}</span><em
												style="pointer-events: none;">${currentDay.get('date')}<span
												style="pointer-events: none;" class="ir">일</span></em><span
												class="day-kr"
												style="pointer-events: none; display: inline-block">${weekDay}</span><span
												class="day-en" style="pointer-events: none; display: none">Tue</span>
										</button>`;
			currentDay=currentDay.add(1,'day');
		}
		$(".date-area .wrap").html(htmlContents);
	}

	$.getJSON("/theater/theaterList", function(locations){
		locations.forEach(function(location, index){
			let contents = '';
			// 로케이션 네임으로 입력할 요소를 찾아서
			let $theatersarea = $('.sel-city:contains('+location.name+') + div ul');
			location.theaters.forEach(function(theater, index){
				// html컨텐츠 만들고
				contents +=`
				<li data-brch-no="${theater.no}">
				<a href="/theater/detail?brchNo=${theater.no}" 
				title="${theater.name} 상세보기">${theater.name}</a></li>
				`
			})
			// 찾은 요소에 대입
			$theatersarea.html(contents);
		})
	})

	// 상영시간표 날짜버튼 클릭시 이벤트 핸들러 등록
	$(".date-area .wrap").on("click","button", function(){
		
	})
	// 극장 상세 탭 버튼 클릭시 
    $(".tab-list a").on("click", function(){
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
	
	$(".theater-place button").on("click", function(){
		$(this).closest("li").addClass("on");
		$(this).closest("li").siblings().removeClass("on");
	})
	
	$(".area-depth1 li").hover(function(){
		$(this).addClass("on");
	},function(){
		$(this).removeClass("on");
	})
	
	/*카카오맵 약도*/
	$(".location-map-btn button").click(function(){
		
		let container = document.getElementById('map');
			let options = {
				center: new kakao.maps.LatLng(33.450701, 126.570667),
				level: 3
			};
	
		let map = new kakao.maps.Map(container, options);
		
		// 주소-좌표 변환 객체를 생성합니다
		var geocoder = new kakao.maps.services.Geocoder();

		// 주소로 좌표를 검색합니다
		geocoder.addressSearch('서울특별시 서초구 서초대로 77길 3 (서초동) 아라타워 8층', function(result, status) {

	    	// 정상적으로 검색이 완료됐으면 
	     	if (status === kakao.maps.services.Status.OK) {
	
		        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
		
		        // 결과값으로 받은 위치를 마커로 표시합니다
		        var marker = new kakao.maps.Marker({
		            map: map,
		            position: coords
		        });
		        marker.setMap(map);
		        
		        // 인포윈도우로 장소에 대한 설명을 표시합니다
		        var  customOverlay = new kakao.maps.CustomOverlay({
					map: map,
				    position: coords,
		            content: `<div class="card card-body p-2" style="width:150px;text-align:center;">
		            			<span>강남메가박스</span>
		            			<div class="px-auto py-2">
		            				<a class="btn btn-primary btn-sm " href="https://map.kakao.com/link/to/Hello World!,${result[0].y},${result[0].x}" style="width:100px; color:white" target="_blank"> 빠른 길찾기</a>
		            			</div>
		           			</div>`,
		            yAnchor: 1.5
		        });
				
				
		        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
		        map.setCenter(coords);
	    	} 
		});    
	})
});

