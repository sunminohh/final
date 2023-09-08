$(() => {

	const API_URLS = {
		DAILY_TOTAL_SALES: "/admin/sales/dailyTotalSales",
		GETSALES:"/admin/sales/getSales"
	}
	const $btnSelect = $("#btnSelect")
	const $period = $(":radio[name='period']")
	const $inputFirstDate = $("#firsttDay")
	const $inputLastDate = $("#lastDay")
	
	const currentDate = dayjs().format("YYYY-MM-DD");
	const oneWeekAgo = dayjs().subtract(7, 'day').format("YYYY-MM-DD");
	$inputFirstDate.val(oneWeekAgo);
	$inputLastDate.val(currentDate);
	const reqData = {};
	$btnSelect.on("click",handlerClickBtnSelect)
	$btnSelect.click();
	
	function handlerClickBtnSelect(){
		const periodVal = $period.filter(":checked").val()
		const firstDate = $inputFirstDate.val()
		const lastDate = $inputLastDate.val()
		reqData.period=periodVal
		reqData.firstDate=firstDate
		reqData.lastDate=lastDate
		console.log(reqData)
		getSales();
	}	

	function getSales(){
		$.ajax({
			url: API_URLS.GETSALES, // 데이터를 가져올 API 엔드포인트
			method: 'post',
			data: JSON.stringify(reqData), // JSON 형태로 변환하여 보냄
		    contentType: "application/json", // JSON 데이터임을 명시
			success: function(response) {
				updateChartWithSelectData(response);
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
	
	function updateChartWithSelectData(data) {
		const days = [];
		const movieNames = [];
		const productNames = [];
		const totalSales = [];
		const movieTotalSales = [];
		const productTotalSales = [];
		const movieSales = [];
		const productSales = [];
		console.log(data)
		if(data.totalSales.length==0){
			Swal.fire({
				icon:"warning",
				text:"매출내역이 없습니다."
			})
			return;
		}
		data.totalSales.forEach(item => {
			// 날짜 데이터를 dayjs로 파싱
			const parsedDate = dayjs(item.date);

			// 월과 일을 가져와서 두 자리로 포맷팅
			const formattedDate = parsedDate.format("MM/DD");

			// 카테고리 배열에 추가
			days.push(formattedDate);

			// 시리즈 데이터 배열에 해당 값 추가
			totalSales.push(item.totalSales);
		});
		data.movieTotalSales.forEach(item => {movieTotalSales.push(item.totalSales);})
		data.productTotalSales.forEach(item => {productTotalSales.push(item.totalSales);})
		
		data.movieSales.forEach(item => {
			movieNames.push(item.name);
			movieSales.push(item.totalSales);
		})
		
		data.productSales.forEach(item => {
			productNames.push(item.name);
			productSales.push(item.totalSales);
		})
		
		chart.updateOptions({
			xaxis: {
				categories: days,
			},
			series: [
				{
					data: totalSales,
				},
				{
					 name: "영화", data: movieTotalSales,
				},
				{
					 name: "상품", data: productTotalSales,
				},
			],
		});
		
		detailmovieChart.updateOptions({
			xaxis: {
				categories: movieNames,
			},
			series: [
				
				{
					 data: movieSales,
				},
			
			],
		});
		
		detailProductChart.updateOptions({
			xaxis: {
				categories: productNames,
			},
			series: [
				
				{
					 data: productSales,
				},
			
			],
		});
	}
	// =====================================
	// 차트
	// =====================================
	// 차트 옵션
	const chartOption = {
		series: [
			{ name: "총매출", data: [180, 355, 390] },
			{ name: "영화", data: [180, 355, 390] },
			{ name: "상품", data: [180, 355, 390] },
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
					reqData.category=seriesName;
					reqData.targetDate=categories;
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
			show: true,
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
				formatter: (val) => {
					return val / 1000000 + 'M'
				},
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
		console.log(data)
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

	const options = {
		series: [
			{
				name: '매출',
				data: [44000, 55000, 41000, 67000, 22000]
			},
			
		],
		chart: {
			type: 'bar',
			height: 350,
			stacked: true,
		},
		stroke: {
			width: 1,
			colors: ['#fff']
		},
		dataLabels: {
			formatter: (val) => {
				return val / 1000 + 'K'
			}
		},
		plotOptions: {
			bar: {
				horizontal: true
			}
		},
		xaxis: {
			categories: [
				'Online advertising',
				'Sales Training',
				'Print advertising',
				'Catalogs',
				'Meetings'
			],
			labels: {
				formatter: (val) => {
					return val / 1000 + 'K'
				}
			}
		},
		fill: {
			opacity: 1,
		},
		colors: ['#80c7fd', '#008FFB', '#80f1cb', '#00E396'],
		legend: {
			position: 'top',
			horizontalAlign: 'left'
		}
	};

	const detailmovieChart = new ApexCharts(document.querySelector("#detailmoviechart"), options);
	detailmovieChart.render();

	const detailProductChart = new ApexCharts(document.querySelector("#detailproductchart"), options);
	detailProductChart.render();




	
	
})
