$(() => {
    const $locationButton = $(".btn-group .btn")
    const $theaters = $(".collapse");
    const $board = $("#board");
    getTheaterList();
    
    $locationButton.on("click",toggleButton);
    $theaters.on("click", "button", toggleTheaterButton);
    $theaters.on("click", "button", refrashBoard);
    
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
		$.getJSON("/admin/theater/detail",{"theaterNo":theaterNo}, function(data){
			console.log(data)
		})
	}
	
	function getTheaterList(){
		$.getJSON("/theater/theaterList", function(locations) {
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
});