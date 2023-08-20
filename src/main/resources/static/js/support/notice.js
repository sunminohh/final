$(function() {

	// Ajax로 요청할 URL 상수로 정의
	const API_URLS = {
		LOCATION: "/support/lost/getLocation",
		THEATER: "/support/lost/getTheaterByLocationNo",
		NOTICE_LIST: "/support/notice/list"
	}

	// 변경되지 않는 여러번 사용될 DOM 객체는 미리 선언해놓고 사용하기
	const $selectLocation = $("#location");
	const $selectTheater = $("#theater");
	const $inputPage = $("input[name=page]");
	const $inputCatNo = $("input[name=catNo]");
	const $inputKeyword = $("input[name=keyword]");

	const $noticeList = $(".noticeList");
	const $pagination = $(".pagination");

	init();

	async function init() {
		await getLocations();
		await getTheatersByLocation(getQueryString("locationNo"));
		initEvents();
		getNoticeList();
	}

	// 나중에 이벤트가 많아질 것을 고려해 이벤트 바인드 함수 분리 및 초기화 함수 추가
	// 바인딩 -> 특정 요소에 이벤트 리스너를 연결, 결합 하는 과정
	function initEvents() {
		// 이벤트 바인딩
		$('li.tab-link').on('click', handleTabClick); // 탭 클릭 이벤트 (21:전체, 22:메가박스공지, 23:지점공지)
		$("#searchBtn").on('click', handleSearchClick); // 검색 버튼 클릭 이벤트
		$("#actionForm").on('submit', handleSearchClick); // 검색 폼 전송 이벤트
		$selectLocation.on('change', handleLocationChange); // 지역 선택 이벤트
		$noticeList.on("click", ".link-detail", addSearchQueryToLink); // 공지 목록 클릭 이벤트
		$pagination.on('click', '.page-number-link', handlePageClick); // 페이지네이션 클릭 이벤트
	}

	// 탭 클릭 핸들러
	function handleTabClick() {
		$('li.tab-link').removeClass('current');
		$('button.btn').removeClass('current');

		$(this).addClass('current');
		$(this).find('button.btn').addClass('current');

		const categoryNo = $(this).find('button').attr("data-category-no");
		$inputCatNo.val(categoryNo);
		$inputPage.val(1);
		getNoticeList();
	}

	// 지역 선택 핸들러
	function handleLocationChange() {
		const locationNo = $(this).val();
		getTheatersByLocation(locationNo);
	}

	// 검색 버튼 클릭 핸들러
	function handleSearchClick(e) {
		e.preventDefault();
		$inputPage.val(1);
		getNoticeList();
	}

	// 페이지네이션 클릭 핸들러
	function handlePageClick(event) {
		event.preventDefault();
		const page = $(this).attr("data-page");
		$('.page-number-link').removeClass('active');
		$(this).addClass('active');
		$inputPage.val(page);
		getNoticeList();
	}

	// 공지 목록 클릭 핸들러, 현재 페이지 QueryString 포함하여 상세 페이지로 이동
	function addSearchQueryToLink() {
		let href = $(this).attr("href");
		href += `&${location.search.replace("?", "")}`;
		$(this).attr("href", href);
	}

	// 지역 조회 및 렌더링
	async function getLocations() {
		const locationNo = getQueryString("locationNo");
		$selectLocation.empty().append('<option value="" selected disabled>지역선택</option>');
		await $.getJSON(API_URLS.LOCATION, function(locations) {
			const options = locations.map(loc =>
				`<option value="${loc.no}" ${String(loc.no) === locationNo ? 'selected' : ''}>
					${loc.name}
				</option>`
			).join("");
			$selectLocation.append(options);
		}).fail(handleAjaxError);
	}

	// 극장 조회 및 렌더링
	async function getTheatersByLocation(locationNo) {
		if (locationNo === null || locationNo === undefined) return;
		const theaterNo = getQueryString("theaterNo");
		$selectTheater.empty().append('<option value="" selected disabled>극장선택</option>');
		await $.getJSON(`${API_URLS.THEATER}?locationNo=${locationNo}`, function(theaters) {
			console.log(locationNo, theaters);
			theaters.forEach(({no}) => console.log(theaterNo, no));
			const options = theaters.map(thr =>
				`<option value="${thr.no}" ${String(thr.no) === theaterNo ? 'selected' : ''}>
					${thr.name}
				</option>`
			).join("");
			$selectTheater.append(options);
		}).fail(handleAjaxError);
	}

	// 공지사항 목록 조회
	function getNoticeList() {
		const requestData = {
			catNo: $inputCatNo.val(),
			locationNo: $selectLocation.val(),
			theaterNo: $selectTheater.val(),
			page: $inputPage.val(),
			keyword: $inputKeyword.val()
		};

		// 쿼리 업데이트 로직
		Object.entries(requestData)
			.forEach(([key, value]) =>
				updateQueryString(key, key.includes("No") ? (value || 0) : value)
			);

		$.getJSON(API_URLS.NOTICE_LIST, requestData, renderNoticeList).fail(handleAjaxError);
	}

	// 공지사항 목록 렌더링
	function renderNoticeList(result) {

		// 비구조화 할당
		const { noticeList, pagination } = result;

		$('#totalCnt').text(pagination.totalRows);

		if (!noticeList.length) {
			$noticeList.html(`<tr><th colspan='5' style="text-align:center;">조회된 내역이 없습니다.</th></tr>`);
			return;
		}

		const noticeHtml = noticeList.map(notice =>
			`<tr>
				<td>${notice.no}</td>
				<td>${notice.theater.name == null ? 'MGV' : notice.theater.name}</td>
				<td>${notice.type === '공지' ? '공지' : '이벤트'}</td>
				<td style="text-align:left;">
					<a class="text-black text-decoration-none link-detail"
					   href="/support/notice/detail?no=${notice.no}">
						${notice.title }
					</a>
				</td>
				<td>${notice.updateDate}</td>
			</tr>`
		).join("\n")

		const paginationHtml = renderPagination(pagination);

		$noticeList.html(noticeHtml);
		$pagination.html(paginationHtml);
	}

	// Ajax 요청 실패 핸들러
	function handleAjaxError() {
		Swal.fire({
			icon: 'error',
			text: "오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
		});
	}
});









