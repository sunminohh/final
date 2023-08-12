
$(function() {
	
	// 탭메뉴	
	$('li.tab-link.current').click();

    $('li.tab-link').click(function() {

      $('li.tab-link').removeClass('current');
      $('button.btn').removeClass('current');

      $(this).addClass('current');
      $(this).find('button.btn').addClass('current');
      
    });
    // 탭메뉴 끝

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
		
		let $selectLocation = $("#location").empty();
		$selectLocation.append(`<option value="" selected disabled>지역선택</option>`)
		
		$.getJSON("/support/one/getLocation", function(locations) {
			locations.forEach(function(loc) {
				let option = `<option value="${loc.no}"> ${loc.name}</option>`;
				$selectLocation.append(option);
			})
		})
	})
	
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
	
})






