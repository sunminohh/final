$(() => {
    const $location = $("#location");
    const $theater = $("#theater");
    const $screen = $("#screen");
    const $date = $("#date");
    const $turn = $("#turn");
    const $movie = $("#movie");
    const $screenInfo = $("#screenInfo")
    
    const $timeInput = $("#timeInput");
    const $minuteInput = $("#minuteInput");
    const $endTime = $("#endTime")
    
    const $btnRead = $("#btnRead")
    const $btnMovie = $("#btnMov")
    const $btnTime = $("#btnTime")
    const $btnReg = $("#btnReg")
    const $table=$("#table-schedule")
    let runTime=0;
    $location.on("change","select",handlerChangeLocation)
    $theater.on("change","select",handlerChangeTheater)
    movieList();
    dateList();
    $btnRead.on("click",handlerClickReadBtn);
    $btnMovie.on("click",handlerClickBtnMovie);
    $btnTime.on("click",handlerClickBtnTime);
    $btnReg.on("click",handlerClickBtnReg);
    
    
    function handlerChangeLocation(){
		let locationNo = $(this).val();
		let $theaterSelect = $theater.find("select").empty();
		let content = `<option value="" selected="" disabled="">극장선택</option>`
		$.getJSON("/admin/theater/theaterList",{"locationNo":locationNo},function(theaters){
			theaters.forEach(function(theater){
				content += `<option value="${theater.no}">${theater.name}</option>`
			})
				$theaterSelect.append(content);
		})
	}
	
    function handlerChangeTheater(){
		let theaterNo = $(this).val();
		let $screenSelect = $screen.find("select").empty();
		let content = `<option value="" selected="" disabled="">상영관선택</option>`
		$.getJSON("/admin/theater/screenList",{"theaterNo":theaterNo},function(screens){
			screens.forEach(function(screen){
				content += `<option value="${screen.id}">${screen.name}</option>`
			})
				$screenSelect.append(content);
		})
	}
	
	function movieList(){
		let $movieSelect = $movie.find("select").empty();
		let content = `<option value="" selected="" disabled="">영화선택</option>`
		$.getJSON("/admin/theater/movieList",function(movies){
			movies.forEach(function(movie){
				content += `<option value="${movie.no}" data-runtime="${movie.runtime}">${movie.title}</option>`
			})
				$movieSelect.append(content);
		})
	}
	
	function dateList(){
		dayjs.locale('ko-kr')
		dayjs.extend(dayjs_plugin_isSameOrAfter)
		let $dateSelect = $date.find("select").empty();
		let content = `<option value="" selected="" disabled="">날짜선택</option>`
		for(i = 0; i<21; i++){
			let date = dayjs().add(i,'day');
			content += `<option value="${date.format('YYYY-MM-DD')}" >${date.format('YYYY-MM-DD dd요일')}</option>`
		}
		$dateSelect.append(content); 
	}
	
	
	function handlerClickReadBtn(){
		let date = $date.find("select").val();
		let screen = $screen.find("select").val();
		const $tbody = $table.find("tbody").empty();
		$.getJSON("/schedule/admin/list",{"date":date,"screen":screen},function(data){
			$turn.find("option").prop("disabled",false).removeClass("disabled");
			data.forEach(function(schedule){
				$turn.find("option[value="+schedule.turn+"]").prop("disabled",true).addClass("disabled");
				let content= `
								<tr>
									<td class="text-center">${schedule.turn}</td>
									<td class="text-center">${schedule.start}</td>
									<td class="text-center">${schedule.end}</td>
									<td class="text-center">${schedule.title}</td>
								</tr>`
				$tbody.append(content);
				
			})
		})
	}
	
	function handlerClickBtnMovie(){
		runTime = $movie.find("select :selected").attr("data-runtime");
	}
	
	function handlerClickBtnTime(){
		let time = parseInt($timeInput.val());
		let minute = parseInt($minuteInput.val());
		$timeInput.val(padZero(time));
		minute = Math.ceil(minute/5)*5;
		$minuteInput.val(padZero(minute));
		time = parseInt(time+((parseInt(minute)+parseInt(runTime)))/60)
		minute = (((parseInt(minute)+parseInt(runTime)))%60)+10
		minute = Math.ceil(minute/5)*5;
		if(minute >= 60){
			time += 1;
			minute = minute-60;
		}
		$endTime.val(padZero(time)+":"+padZero(minute))
	}
	
	function handlerClickBtnReg(){
		let schedule = {
			    locationNo: $location.find("select").val(),
			    theaterNo: $theater.find("select").val(),
			    screenId: $screen.find("select").val(),
			    date: $date.find("select").val(),
			    movieNo: $movie.find("select").val(),
			    startTime: $timeInput.val() + ":" + $minuteInput.val(),
			    endTime: $endTime.val(),
			    turn: $turn.find("select").val(),
			    screenInfo: $screenInfo.find("select").val()
			};
		if (!checkForm(schedule)) {
        	return; // 유효성 체크 실패 시, AJAX 요청을 보내지 않고 종료
    	}
		console.log(schedule);
		$.ajax({
            url: "/schedule/admin/regist",
            type: "POST",
            data: schedule,
            data: JSON.stringify(schedule), // JSON 형태로 변환하여 보냄
		    contentType: "application/json", // JSON 데이터임을 명시
		    success: function(response) {
				console.log(response);
		        if(response =='timeduplicated'){
					callMessage(6);
				}
		        if(response =='fail'){
					callMessage(10);
				}
		        if(response =='success'){
					callMessage(11);
					$btnRead.click();
					
				}
			}
        }).fail(function(){
			callMessage(10);
		});
	}

	function checkForm(schdule){

		if(schdule.locationNo === null){
			callMessage(0);
			return false;
		}
		if(schdule.theaterNo === null){
			callMessage(1);
			return false;
		}
		if(schdule.screenId === null){
			callMessage(2);
			return false;
		}
		if(schdule.date === null){
			callMessage(3);
			return false;
		}
		if(schdule.movieNo === null || runTime == 0){
			callMessage(4);
			return false;
		}
		if(schdule.startTime.length<4){
			callMessage(5);
			return false;	
		}
		if(schdule.endTime.length<4){
			callMessage(7);
			return false;	
		}
		if(schdule.turn == null || schdule.turn.length<1){
			callMessage(8);
			return false;
		}
		if(schdule.screenInfo == null){
			callMessage(9);
			return false;
		}
		return true;

	}
	
	function callMessage(messageNo){
		let message =[
			"지역을 선택해주세요",
			"극장을 선택해주세요",
			"상영관을 선택해주세요",
			"상영일자를 선택해주세요 ",
			"영화를 선택해주세요",
			"시작시간을 지정해주세요",
			"시작시간을 다시 지정해주세요",
			"종료시간을 다시 확인해주세요",
			"회차를 선택해주세요",
			"상영정보를 선택해주세요",
			"등록중 오류 관리자에게 문의 하세요.",
			"등록 되었습니다.",
		]
		if(messageNo<=10){
			Swal.fire({
				icon:"error",
				text: message[messageNo]
			})
		}
		if(messageNo>10){
			Swal.fire({
				icon:"success",
				text: message[messageNo]
			})
			
		}
	}
	
	function padZero(number) {
	  return (number < 10 ? '0' : '') + number;
	}
    
});