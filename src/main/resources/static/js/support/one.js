$(function() {
	
	$("#onesupport").change(function() {
		let $select = $("#oneCat").empty();
		$select.append(`<option value="" selected disabled>문의유형 선택</option>`)
		
		$.getJSON("/support/one/getCategory?type=고객센터문의", function(categories) {
			categories.forEach(function(cat) {
				let option = `<option value="${cat.no}"> ${cat.name}</option>`;
				$select.append(option);
			})
		})
	}).trigger("change");
	
	$("#onetheater").change(function() {
		let $select = $("#oneCat").empty();
		$select.append(`<option value="" selected disabled>문의유형 선택</option>`)
		
		$.getJSON("/support/one/getCategory?type=극장별문의", function(categories) {
			categories.forEach(function(cat) {
				let option = `<option value="${cat.no}"> ${cat.name}</option>`;
				$select.append(option);
			})
		})
	})
	
})