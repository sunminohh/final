$(() => {
	const $locations = $("select[name='location']");
	const $theater = $("input[name='theater-name']");
	const $address = $("input[name='address']");
	const $tel = $("input[name='tel']");
    

	const $parkinginfo = $("textarea[name='parkinginfo']")
	const $parkingconfirm = $("textarea[name='parkingconfirm']")
	const $parkingcash = $("textarea[name='parkingcash']")
	const $theaterInfo = $("textarea[name='theaterInfo']")
	
    const $floorBox1 = $("#floorBox1")
    const $floorBox2 = $("#floorBox2")
    const $btnAddFloorInput = $("#btnAddFloorInput")
    const $btnReg = $("#btnReg");
    
    
    $btnAddFloorInput.click(handlerBtnAddFloorInput);
    $floorBox2.on("click","button", handlerBtnDelInput);
    $("form").submit(function(e){
		e.preventDefault();
		registTheater();
	} );
    refrashBoard();
    
    
    function handlerBtnAddFloorInput(){
		let htmlContent=`<div class="d-flex py-1">
							<input type="text" class="form-control" style="width: 25%"
																	placeholder="예:1층, 로비층" aria-label="floor" name="floor" required="required"> 
																<input type="text"
																	class="form-control" placeholder="안내사항"
																	aria-label="floorinfo" name="floorinfo" required="required">
							
							<button type="button" class=" ms-1 btn btn-danger text-nowrap">삭제</button>
						</div>
						`
		$floorBox2.append(htmlContent);
	}
	
	function handlerBtnDelInput(){
		$(this).closest("div").remove();
	}
	
	function refrashBoard(){
		let theaterNo = $("h5").attr("data-theater-no");
		$.getJSON("/admin/theater/detail",{"theaterNo":theaterNo}, function(data){
			console.log(data)
			let $abled = $("form");
			$abled.find("[name=location]").val(data.location.name)
			$abled.find("[name=theater-name]").val(data.name)
			$abled.find("[name=address]").val(data.address)
			$abled.find("[name=tel]").val(data.tel)
			data.facilities.forEach(function(facility, index){
				let check = $abled.find("[name=facility]").val() == facility.type;
				$abled.find("[name=facility]").prop("checked", check)
			});
			$abled.find("[name=floor]").empty();
			data.floorInfos.forEach(function(floorInfo, index){
				if(index == 0){
					$floorBox1.find("[name=floor]").val(floorInfo.floor)	
					$floorBox1.find("[name=floorinfo]").val(floorInfo.info)	
				}else{
					handlerBtnAddFloorInput();
					$floorBox2.find("[name=floor]").eq(index-1).val(floorInfo.floor)	
					$floorBox2.find("[name=floorinfo]").eq(index-1).val(floorInfo.info)	
				}
				let content = `<tr><td>${floorInfo.floor}:${floorInfo.info}</td></tr>`
				$abled.find("[name=floor]").append(content)
			})
			$abled.find("[name=parkinginfo]").text(data.parkingInfo.info)
			$abled.find("[name=parkingconfirm]").text(data.parkingInfo.confirm)
			$abled.find("[name=parkingcash]").text(data.parkingInfo.cash)
			$abled.find("[name=theaterInfo]").text(data.info)
		})
	}
	
	
	function registTheater(){
		
		let $floor = $("input[name='floor']")
		let $floorInfo = $("input[name='floorinfo']")
		let $facilitySelect = $("input[type='checkbox']:checked")
		let floors = [];
		let floorInfos = [];
		let facilities = [];
		let locationNo = $locations.val();
		let theaterName = $theater.val();
		let theaterNo = $("h5").attr("data-theater-no");
		let address = $address.val();
		let tel = $tel.val();
		let parkingcash = $parkingcash.val();
		let parkingconfirm = $parkingconfirm.val();
		let parkinginfo = $parkinginfo.val();
		let theaterInfo = $theaterInfo.val();
		
		$floor.each(function(){
			floors.push($(this).val());
		})
		$floorInfo.each(function(){
			floorInfos.push($(this).val());
		})
		$facilitySelect.each(function(){
			let facility = {};
			facility.type = $(this).val();
			facilities.push(facility);
			console.log(facility)
			console.log(facilities)
		})
		
		
		
		let theater = {
	        location: {
	            no: locationNo // location 객체의 no 필드
	        },
	        no: theaterNo,
	        name: theaterName,
	        address: address,
	        tel: tel,
	        facilities: facilities,
	        parkingInfo: {
				info:parkinginfo,
				confirm:parkingconfirm,
				cash:parkingcash
	        },
	        floorInfos: floorInfos.map((info, index) => {
            	return {
	                floor: floors[index], // 해당 floor의 값
	                info: info // 해당 floor의 info 값
	            };
        	}),
	        info: theaterInfo
   		};
   		console.log(theater);
   		$.ajax({
            type: "POST",
			url: '/admin/theater/modify',
			contentType: 'application/json',
			data: JSON.stringify(theater),
					success: function(data) {
						if (data.status === 'success') {
							Swal.fire({
								icon: 'success',
								title: '등록 성공',
								text: '극장 정보가 수정되었습니다.',
								confirmButtonText: '확인'
							}).then(() => {
								location.reload();
							});
						} else if (data.status === 'fail') {
							Swal.fire({
								icon: 'error',
								title: '등록 실패',
								text: data.message,
								confirmButtonText: '확인'
							});
						} else {
							Swal.fire({
								icon: 'info',
								title: '알 수 없는 상태',
								text: '서버로부터 알 수 없는 응답을 받았습니다.',
								confirmButtonText: '확인'
							});
						}
					},
					error: function(error) {
						Swal.fire({
								icon: 'error',
								title: '네트워크 오류',
								text: error,
								confirmButtonText: '확인'
							});
					}
			
		});
	}
	

	
	
});