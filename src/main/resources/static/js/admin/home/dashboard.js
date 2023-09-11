$(() => {
	const $bookingList=$("#bookingList")
	const $orderList=$("#orderList")
	const $btnRefreshOrder=$("#btn-refresh-order")
	const $btnRefreshBooking=$("#btn-refresh-booking")
	const currentDate = dayjs().format("YYYY-MM-DD");
	const oneWeekAgo = dayjs().subtract(7, 'day').format("YYYY-MM-DD");
	const reqData={
		lastDate:currentDate,
		firstDate:oneWeekAgo
	}
	const API_URLS = {
		DAILY_TOTAL_SALES: "/admin/sales/dailyTotalSales",
		BOOKINGLIST:"/api/booking/getBookingList",
		ORDERLIST:"/api/order/orderList"
	}
	
	$btnRefreshBooking.click(handlerBtnRefreshBookingList);
	$btnRefreshOrder.click(handlerBtnRefreshOrderList);
	$btnRefreshBooking.click();
	$btnRefreshOrder.click();
	// =====================================
	// Profit
	// =====================================
	// 차트 옵션
	const chartOption = {
		series: [
			{ name: "매출", data: [180, 355, 390] },
		],
		chart: {
			type: "bar",
			height: 345,
			offsetX: -15,
			toolbar: { show: true },
			foreColor: "#adb0bb",
			fontFamily: 'inherit',
			sparkline: { enabled: false },
			
		},
		colors: ["#5D87FF", "#49BEFF"],
		plotOptions: {
			bar: {
				horizontal: false,
				columnWidth: "35%",
				borderRadius: [6],
				borderRadiusApplication: 'end',
				borderRadiusWhenStacked: 'all',
			},
		},
		markers: { size: 0 },
		dataLabels: {
			enabled: false,
		},
		legend: {
			show: false,
		},
		grid: {
			borderColor: "rgba(0,0,0,0.1)",
			strokeDashArray: 3,
			xaxis: {
				lines: {
					show: false,
				},
			},
		},
		xaxis: {
			type: "category",
			categories: ["16/08", "17/08", "18/08", "19/08", "20/08", "21/08", "22/08", "23/08"],
			labels: {
				style: { cssClass: "grey--text lighten-2--text fill-color" },
			},
		},
		yaxis: {
			show: true,
			min: 0,
			tickAmount: 4,
			labels: {
				style: {
					cssClass: "grey--text lighten-2--text fill-color",
				},
			},
		},
		

		stroke: {
			show: true,
			width: 3,
			lineCap: "butt",
			colors: ["transparent"],
		},
		tooltip: { theme: "light" },
		responsive: [
			{
				breakpoint: 600,
				options: {
					plotOptions: {
						bar: {
							borderRadius: 3,
						}
					},
				}
			}
		]
	};

	const chart = new ApexCharts(document.querySelector("#chart"), chartOption);
	chart.render();
	
	function updateChartWithData(data) {
		const categories = []
		const seriesData = []
		
		data.forEach(item => {
			// 날짜 데이터를 dayjs로 파싱
			const parsedDate = dayjs(item.date);

			// 월과 일을 가져와서 두 자리로 포맷팅
			const formattedDate = parsedDate.format("MM/DD");

			// 카테고리 배열에 추가
			categories.push(formattedDate);

			// 시리즈 데이터 배열에 해당 값 추가
			seriesData.push(item.totalSales);
		});
		
		chart.updateOptions({
			xaxis: {
				categories: categories,
			},
			series: [
				{
					data: seriesData,
				},
			],
		});
	}

	$.ajax({
		url: API_URLS.DAILY_TOTAL_SALES, // 데이터를 가져올 API 엔드포인트
		method: 'post',
		data: JSON.stringify(reqData), // JSON 형태로 변환하여 보냄
		contentType: "application/json", // JSON 데이터임을 명시
		success: function(data) {
			console.log(data)
			updateChartWithData(data);
		},
		error: function(error) {
			Swal.fire({
				icon:"error",
				text:"네트워크 요청 오류, 잠시후에 다시 시도 하세요."
			})
			// 에러 처리: 사용자에게 메시지를 표시하거나 적절한 조치를 취하세요.
		}
	});
	function handlerBtnRefreshBookingList(){
		$.ajax({
			url: API_URLS.BOOKINGLIST, // 데이터를 가져올 API 엔드포인트
			method: 'GET',
			dataType: 'json',
			success: function(data) {
				console.log(data)
				refreshBookingList(data)
			},
			error: function(error) {
				Swal.fire({
					icon:"error",
					text:"네트워크 요청 오류, 잠시후에 다시 시도 하세요."
				})
				// 에러 처리: 사용자에게 메시지를 표시하거나 적절한 조치를 취하세요.
			}
		});
	}
	function handlerBtnRefreshOrderList(){
		$.ajax({
			url: API_URLS.ORDERLIST, // 데이터를 가져올 API 엔드포인트
			method: 'GET',
			dataType: 'json',
			success: function(data) {
				console.log(data)
				refreshOrderList(data)
			},
			error: function(error) {
				Swal.fire({
					icon:"error",
					text:"네트워크 요청 오류, 잠시후에 다시 시도 하세요."
				})
				// 에러 처리: 사용자에게 메시지를 표시하거나 적절한 조치를 취하세요.
			}
		});
	}
	function refreshBookingList(data){
		$bookingList.empty();
		data.forEach(function(item){
			
			const timelineItem=`<li class="timeline-item d-flex position-relative overflow-hidden">
	                                <div class="timeline-time text-dark flex-shrink-0 text-end">${dayjs(item.updateDate).format('DD일 HH:mm')}</div>
	                                <div class="timeline-badge-wrap d-flex flex-column align-items-center">
	                                    <span class="timeline-badge border-2 border ${item.bookingState.endsWith('완료') ? 'border-primary':'border-danger'} flex-shrink-0 my-8"></span>
	                                </div>
	                                <div class="timeline-desc fs-3 text-dark mt-n1">${item.title} ${item.payAmount}원 ${item.bookingState}</div>
	                            </li>`
			$bookingList.append(timelineItem);
		})
	}
	function refreshOrderList(data){
		$orderList.empty();
		data.forEach(function(item){
			
			const timelineItem=`<li class="timeline-item d-flex position-relative overflow-hidden">
	                                <div class="timeline-time text-dark flex-shrink-0 text-end">${dayjs(item.updateDate).format('DD일 HH:mm')}</div>
	                                <div class="timeline-badge-wrap d-flex flex-column align-items-center">
	                                    <span class="timeline-badge border-2 border ${item.orderState.endsWith('완료') ? 'border-primary':'border-danger'} flex-shrink-0 my-8"></span>
	                                </div>
	                                <div class="timeline-desc fs-3 text-dark mt-n1">${item.orderName} ${item.totalPrice}원 ${item.orderState}</div>
	                            </li>`
			$orderList.append(timelineItem);
		})
	}

})
