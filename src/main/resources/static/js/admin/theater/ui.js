$(() => {
    const $locationButton = $(".btn-group .btn")
    const $theaters = $(".collapse");
    const $board = $("#board");
    const $btnSchedule = $("#btn-schedule");
    const $btnUpdate = $("#btn-update");
    const $btnRegSchedule = $("#btn-reg-sche");
    const $btnDelSchedule = $("#btn-del-sche");
    const API_URLS = {
		SCHEDULE: "/admin/theater/schedule/list",
		REGISTSCHEDULE: "/admin/theater/schedule/register",
		DELETESCHEDULE: "/admin/theater/schedule/delete",
		THEATER: "/admin/theater/detail",
		THEATER_LIST: "/theater/theaterList"
	}
    getTheaterList();
    
    $locationButton.on("click",toggleButton);
    $theaters.on("click", "button", toggleTheaterButton);
    $theaters.on("click", "button", refrashBoard);
    $btnSchedule.on("click",handlerBtnSchedule);
    $btnRegSchedule.on("click",handlerBtnRegSchedule);
    $btnDelSchedule.on("click",handlerBtnDelSchedule);
    /*$btnUpdate.on("click",handlerBtnUpdate);*/
    
    function toggleButton(){
		$(this).siblings().removeClass("active");
		$(this).addClass("active");
	}
	
	function toggleTheaterButton(){
		$theaters.find("button").removeClass("active");
		$(this).addClass("active");
	}
	
	
	function refrashBoard(){
		let theaterNo = $theaters.find(".active").attr("data-theater-no");
		$board.find("#disabled").addClass("d-none");
		$board.find("#abled").removeClass("d-none");
		$.getJSON(API_URLS.THEATER,{"theaterNo":theaterNo}, function(data){
			console.log(data)
			let $abled = $board.find("#abled");
			$abled.find("[name=location]").text(data.location.name)
			$abled.find("[name=theater-name]").text(data.name)
			$abled.find("[name=address]").text(data.address)
			$abled.find("[name=tel]").text(data.tel)
			$abled.find("[name=facility]").text(data.facilities[0].name)
			$abled.find("[name=floor]").empty();
			data.floorInfos.forEach(function(floorInfo){
				let content = `<tr><td>${floorInfo.floor}:${floorInfo.info}</td></tr>`
				$abled.find("[name=floor]").append(content)
			})
			$abled.find("[name=parkinginfo]").text(data.parkingInfo.info)
			$abled.find("[name=parkingconfirm]").text(data.parkingInfo.confirm)
			$abled.find("[name=parkingcash]").text(data.parkingInfo.cash)
			$abled.find("[name=info]").text(data.info)
		})
	}
	
	function getTheaterList(){
		$.getJSON(API_URLS.THEATER_LIST, function(locations) {
			locations.forEach(function(location) {
				let contents = '';
				let $theatersArea = $("#theaters-"+location.no+" .col-12"); 
				location.theaters.forEach(function(theater) {
					// html컨텐츠 만들고
					contents += `
					<button type="button" class="btn btn-outline-primary" data-theater-no="${theater.no}"
										title="${theater.name} 상세보기" style="width: 100px;">${theater.name}</button>
					`
				})
				// 찾은 요소에 대입
				$theatersArea.html(contents);
			})
		}).fail(function(){
			Swal.fire({
				icon: "error",
				text: "잠시 후 다시 시도 해 주세요."
			})
		})
	}
	
	function handlerBtnSchedule(){
		let theaterNo = $theaters.find(".active").attr("data-theater-no");
		if(theaterNo){
			window.location.href=API_URLS.SCHEDULE+"?theaterNo="+theaterNo;
		}else{
			Swal.fire({
				icon:"error",
				text:"지역과 극장을 선택해주세요.",
			})
		}
	}
	
	function handlerBtnRegSchedule(){
		window.location.href=API_URLS.REGISTSCHEDULE;
	}
	
	function handlerBtnDelSchedule(){
		window.location.href=API_URLS.DELETESCHEDULE;
	}
	
});