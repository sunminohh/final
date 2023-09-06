$(() => {
	

	// =====================================
	// Profit
	// =====================================
	const API_URLS = {
		DAILY_TOTAL_SALES: "/admin/sales/dailyTotalSales",
	}
	// 차트 옵션
	const chartOption = {
		series: [
			{ name: "매출", data: [180, 355, 390] },
			{ name: "영화매출", data: [180, 355, 390] },
			{ name: "상품매출", data: [180, 355, 390] },
		],
		chart: {
			type: "bar",
			height: 345,
			offsetX: -15,
			toolbar: { show: true },
			foreColor: "#adb0bb",
			fontFamily: 'inherit',
			sparkline: { enabled: false },
			events: {
			    dataPointSelection: function (event, chartContext, config) {
			        console.log(config);
			        const dataPointIndex = config.dataPointIndex;
					const seriesIndex = config.seriesIndex;
					const value = chartContext.w.globals.series[seriesIndex][dataPointIndex];
					const categories = chartContext.w.globals.labels[dataPointIndex]; // 카테고리 값 얻기
					const seriesName = chartContext.w.config.series[seriesIndex].name; // 시리즈 이름 얻기
					console.log(`클릭한 데이터 포인트: ${dataPointIndex}, 시리즈: ${seriesName}, 카테고리: ${categories}, 값: ${value}`);
		    },
		},
		},
		colors: ["#5D87FF", "#49BEFF","#1fffff"],
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
				{
					data: seriesData,
				},
				{
					data: seriesData,
				},
			],
		});
	}

	$.ajax({
		url: API_URLS.DAILY_TOTAL_SALES, // 데이터를 가져올 API 엔드포인트
		method: 'GET',
		dataType: 'json',
		success: function(data) {
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

	
})
