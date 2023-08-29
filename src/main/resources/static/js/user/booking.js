$(() => {
    const $booking = $("#booking-tab");
    const $pay = $("#pay-tab");

    $('.btn.booking, .btn.pay').on('click', function (e) {
        e.preventDefault();

        // Remove "on" class from all tab buttons
        $('.btn.booking, .btn.pay').parent().removeClass('on');

        // Add "on" class to the clicked tab button
        $(this).parent().addClass('on');

        // Hide all tab contents
        $('.tab-cont').removeClass('on');

        // Get the target tab ID from the href attribute
        const targetTab = $(this).attr('href');

        $(targetTab).addClass('on');

    })

    $(".btn-period .btn").on("click", function() {
        // 기존 버튼에 있는 "on" 클래스 제거
        $(".btn-period .btn").removeClass("on");

        // 클릭한 버튼에 "on" 클래스 추가
        $(this).addClass("on");

        const period = $(this).val(); // 버튼 값 (D7, M1, M3, M6)
        let today = new Date();
        let startDate, endDate;

        if (period === "D7") {
            startDate = new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000);
        } else if (period === "M1") {
            startDate = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
        } else if (period === "M3") {
            startDate = new Date(today.getFullYear(), today.getMonth() - 3, today.getDate());
        } else if (period === "M6") {
            startDate = new Date(today.getFullYear(), today.getMonth() - 6, today.getDate());
        }

        endDate = today;

        // 날짜 형식을 "yyyy.mm.dd"로 변환하여 입력 필드에 설정
        $("#startDate").val(formatDate(startDate));
        $("#endDate").val(formatDate(endDate));

        $("#searchButton").on("click", function() {
            let startDate = $("#startDate").val();
            let endDate = $("#endDate").val();

            // todo 이후 백엔드로 데이터 조회 요청을 보내는 작업 수행
            // 예: AJAX 요청을 통해 서버로 startDate와 endDate 전송
        });
    });

    // "구매" 탭을 클릭하면 1개월 버튼에 "btn on" 클래스를 추가하고,
    // 시작 날짜 및 종료 날짜 입력 필드에 기본값을 설정하는 함수
    function activateOneMonth() {

        // 현재 날짜를 기준으로 1개월 전 날짜를 계산
        let today = new Date();
        let oneMonthAgo = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());

        // 날짜 형식을 "yyyy.mm.dd"로 변환
        let startDateStr = formatDate(oneMonthAgo);
        let endDateStr = formatDate(today);

        // 날짜를 각각의 입력 필드에 설정
        $("#startDate").val(startDateStr);
        $("#endDate").val(endDateStr);
    }

    // 날짜를 "yyyy.mm.dd" 형식으로 변환하는 함수
    function formatDate(date) {
        let year = date.getFullYear();
        let month = (date.getMonth() + 1).toString().padStart(2, "0");
        let day = date.getDate().toString().padStart(2, "0");
        return year + "." + month + "." + day;
    }

    // "구매" 탭 클릭 시 동작 설정
    $(".tab-block a.pay").on("click", function() {
        activateOneMonth();
    });

    // 초기에도 1개월 기간을 설정
    activateOneMonth();


    // todo 예매


    // todo 구매
})

// datepicker 설정
$(() => {
    $(".datepicker").datepicker({
        dateFormat: 'yy-mm-dd' //달력 날짜 형태
        ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
        ,showMonthAfterYear:true // 월- 년 순서가아닌 년도 - 월 순서
        ,changeYear: true //option값 년 선택 가능
        ,changeMonth: true //option값  월 선택 가능
        ,showOn: "button" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시
        ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
        ,buttonImageOnly: true //버튼 이미지만 깔끔하게 보이게함
        ,buttonText: "선택" //버튼 호버 텍스트
        ,yearSuffix: "년" //달력의 년도 부분 뒤 텍스트
        ,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 텍스트
        ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip
        ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 텍스트
        ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 Tooltip
        ,yearRange: "-100:+0" // 선택 가능한 년도 범위 (현재부터 100년 전부터 현재까지)
    });
})