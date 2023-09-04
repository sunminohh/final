$(() => {

	
	
	let selectedScreen;
	let mouseoverSeat;
	let selectedSeat;
	const seatChoices = new Set;
	const $btnSeatSet = $("#btn-seat-set");
	const $btnSeatCal = $("#btn-seat-cal");
	const $btnReg = $("#btnReg");
	const $seat = $("#seatsQuantity")
	const $location = $("#select-location")
	const $theater = $("#select-theater")
	const $screenInfo =$("#screenName")
	const $screenRow=$("#screenRow");	//행
	const $screenCol=$("#screenCol");	//열
	
	let screenId;
	let screenRow=0;	//행
	let screenCol=0;	//열
	
	$btnSeatSet.on("click",function(){
		init();
	})
	
	$btnSeatCal.on("click",function(){
		const params = []
		const $emptySeat = $(".empty")
		$emptySeat.each(function() {
			params.push($(this).attr('id'))
		})
		const screenSeatsQuantity = screenRow * (screenCol - 1) - (params.length)
		$seat.val(screenSeatsQuantity)
	})
	
	$location.change(handlerChangeLocation);
	
	init();
	function init(){
		screenRow=$screenRow.val();
		if(screenRow > 26){
			screenRow = 26;
			$screenRow.val(screenRow)
		}
		screenCol=(parseInt($screenCol.val())+1);
		if(screenCol > 30){
			screenCol = 31;
			$screenCol.val(screenCol)
		}
		initDefaultSeats(screenRow, screenCol)
	}

	function initDefaultSeats(screenRow, screenCol) {
		const seatsDiv = $("#seatsDiv")
		seatsDiv.empty()
		for (let i = 0; i < screenRow; i++) {
			let rowChar = String.fromCharCode(65 + i)
			seatsDiv.append('<div class="row justify-content-center"></div>')
			let row = seatsDiv.children(":last-child")
			row.append(createFirstSeat(rowChar))
			for (let j = 1; j < screenCol; j++) {
				let seatNo = rowChar + j
				row.append(createActiveSeat(seatNo, i, j, "standard common"))
			}
		}
			seatNumbering(screenRow, screenCol)
		fetch("api/booking/deleteDisabledSeats?screenId=" + screenId).then(res => console.log(res))
	}
	
	
	function seatNumbering(screenRow, screenCol) {
		const seatsDiv = $("#seatsDiv")
		const allEmptySeats = new Set
		const noIndexCols = new Set
		const emptySeats = $(".empty")
		emptySeats.each(function(i, seat) {
			allEmptySeats.add($(this).attr('id'))
		})
		for (let i = 1; i < screenCol + 1; i++) {
			if (emptySeats.filter("[c=" + i + "]").length == screenRow) {
				noIndexCols.add(i)
			}
		}
		for (let i = 0; i < screenRow; i++) {
			let index = 1
			let rowChar = String.fromCharCode(65 + i)
			let row = seatsDiv.children(":eq(" + i + ")")
			for (let j = 1; j < screenCol; j++) {
				if (noIndexCols.has(j)) {
					continue
				}
				let seatNo = rowChar + j
				if (allEmptySeats.has(seatNo)) {
					index++
					continue
				}
				let seat = $("#" + seatNo)
				seat.attr('title', rowChar + index)
				let span = seat.children().filter(':first').text(index++)
			}
		}
	}

	function createFirstSeat(rowChar) {
		return `<button type="button" class="btn-seat-row" title="${rowChar} 행" >${rowChar}</button>`
	}

	function createActiveSeat(seatNo, screenRow, screenCol, state) {
		return `<button type="button" title="${seatNo} (스탠다드/일반)" class="jq-tooltip seat-condition ${state}" id=${seatNo} r=${screenRow} c=${screenCol} seatclasscd="GERN_CLS" seatzonecd="GERN_ZONE" seatuniqno="00100101" rownm="A" seatno="1" seatchoidircval="1" seatchoigrpno="2" seatchoigrpnm="A2" seatchoirowcnt="6" seatchoigrpseq="1" seattocnt="1">
                                     <span class="num">${seatNo}</span>
                                    <span class="rank">일반</span>
                                                    </button>`
	}
	function createDisabledSeat(seatNo, screenRow, screenCol) {
		return `<div class="empty" id="${seatNo}" r="${screenRow}" c="${screenCol}" style="width:18px; height:18px; padding:0px;"></div>`
	}

	function handlerChangeLocation(){
		let locationNo = $(this).val();
		$theater.empty();
		let content = `<option value="" selected="" disabled="">극장선택</option>`
		$.getJSON("/admin/theater/theaterList",{"locationNo":locationNo},function(theaters){
			theaters.forEach(function(theater){
				content += `<option value="${theater.no}">${theater.name}</option>`
			})
				$theater.append(content);
		})
	}

	$("#seatsDiv").on('click', 'button', function() {
		const id = $(this).attr('id')
		if (selectedSeat) {
			if (selectedSeat == id) {
				clearSeatChoices()
				return
			} else chooseSeats()
		} else {
			selectedSeat = id
			$(this).addClass('choice')
			return;
		}
	})
	$("#seatsDiv").on('mouseover', 'button', function() {
		if (seatChoices.size > 0) {
			return
		}
		mouseoverSeat && mouseoverSeat.removeClass('on')
		mouseoverSeat = $(this)
		mouseoverSeat.addClass('on')
		const id = $(this).attr('id')
		if (selectedSeat) {
			if (selectedSeat == id) {
				return
			} else selectSeats($(this).attr('r'), $(this).attr('c'))
		} else {
			return;
		}
	})
	
	function selectSeats(screenRow, screenCol) {
		$("#seatsDiv .jq-tooltip").removeClass('on')
		const sr = $("#" + selectedSeat).attr('r')
		const sc = $("#" + selectedSeat).attr('c')
		const startRow = Math.min(sr, screenRow)
		const endRow = Math.max(sr, screenRow) + 1
		const startCol = Math.min(sc, screenCol)
		const endCol = Math.max(sc, screenCol) + 1

		const $addonSeat = $("#seatsDiv").children().slice(startRow, endRow)
		// const y='.jq-tooltip.seat-condition.standard.common'
		$addonSeat.each(function(index, el) {
			$(this).children().slice(startCol, endCol).addClass('on')
		})
	}

	function chooseSeats() {
		const seats = $("#seatsDiv .on")
		seats.each(function(index, el) {
			$(this).addClass('choice')
			seatChoices.add($(this).attr('id'))
		})
	}
	function clearSeatChoices() {
		const onSeats = $("#seatsDiv .on").removeClass('on')
		const choiceSeats = $("#seatsDiv .choice").removeClass('choice')
		selectedSeat = undefined
		seatChoices.clear()
	}
	// $(this).replaceWith(`<div class="empty" style="width:18px; height:18x; padding:0px;"></div>`)

	$("#emptySeat").on('click', () => {
		const seats = $("#seatsDiv .choice")
		seats.each(function(i, e) {
			$(this).replaceWith(createDisabledSeat($(this).attr('id'), $(this).attr('r'), $(this).attr('c')))
		})
		clearSeatChoices()
		seatNumbering(screenRow, screenCol)
	})

	$("#seatSoftReset").on('click', () => {
		clearSeatChoices()
		$("#seatsDiv").empty()
		init();
	})
	$("#emptyChoice").on('click', () => {
		clearSeatChoices()
	})
	
	$btnReg.on('click', (e) => {
		e.preventDefault();
		const disabledSeats = [] 
		const theaterNo = $theater.val();
		const screenName = $screenInfo.val();
		const screenSeatsQuantity = $seat.val();
		
		const $emptySeats = $(".empty")
		$emptySeats.each(function(i, e) {
			disabledSeats.push($(this).attr('id'))
		})
		
		const data ={
			screen:{
				theater:{
					no:theaterNo
				},
				name: screenName,
				screenRow:screenRow,
				screenCol:screenCol,
				seats:screenSeatsQuantity
			},
			disabledSeats:disabledSeats
			
		}
		
		$.ajax({
            type: "POST",
			url: '/admin/theater/screen/regist',
			contentType: 'application/json',
			data: JSON.stringify(data),
            success: function(response) {
				if (response.status === 'success') {
					Swal.fire({
						icon: 'success',
						title: '등록 성공',
						text: '상영관정보가 등록되었습니다.',
						confirmButtonText: '확인'
					}).then(() => {
						location.reload();
					});
				} else if (response.status === 'fail') {
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
			}
		
		})
	})
	

	$("#mCSB_1_container222").on('mouseleave', () => {
		if (selectedSeat) {
			const onSeats = $("#seatsDiv .on").removeClass('on')
		}
	})
	
	
	
})
	
	
