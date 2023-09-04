$(() => {
	const $locations = $("select[name='location']");
	const $theater = $("input[name='theater-name']");
	const $address = $("input[name='address']");
	const $tel = $("input[name='tel']");
    const $facilitySelect = $("select[name='facility']");

	const $parkinginfo = $("textarea[name='parkinginfo']")
	const $parkingconfirm = $("textarea[name='parkingconfirm']")
	const $parkingcash = $("textarea[name='parkingcash']")
	const $theaterInfo = $("textarea[name='theaterInfo']")
	
    const $floorBox = $("#floorBox")
    const $btnAddFloorInput = $("#btnAddFloorInput")
    const $btnReg = $("#btnReg");
    
    
    $btnAddFloorInput.click(handlerBtnAddFloorInput);
    $floorBox.on("click","button", handlerBtnDelInput);
    $("form").submit(function(e){
		e.preventDefault();
		registTheater();
	} );
    refrashFaciltyInput();
    
    
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
		$floorBox.append(htmlContent);
	}
	
	function handlerBtnDelInput(){
		$(this).closest("div").remove();
	}
	
	function refrashFaciltyInput(){
		let htmlContent = `<option selected disabled>선택하세요</option>`
		
		$.getJSON("/admin/theater/registform",function(data){
			data.forEach(function(facility){
				htmlContent += `<option value="${facility.type}" >${facility.name}</option>`;			
			})
		$facilitySelect.html(htmlContent);
		})
	}
	
	
	function registTheater(){
		
		let $floor = $("input[name='floor']")
		let $floorInfo = $("input[name='floorinfo']")
		let floors = [];
		let floorInfos = [];
		let facilities = [];
		let locationNo = $locations.val();
		let theaterName = $theater.val();
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
			facility.type = $facilitySelect.val();
			facilities.push(facility);
		})
		
		
		
		let theater = {
	        location: {
	            no: locationNo // location 객체의 no 필드
	        },
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
			url: '/admin/theater/regist',
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