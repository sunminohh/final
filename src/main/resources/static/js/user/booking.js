$(() => {
    $('.tab-cont').hide(); // 모든 탭 컨텐츠 숨기기
    $('#booking-tab').show(); // 예매 탭 컨텐츠만 보이기

    $('.tab-block a').click(function(e) {
        e.preventDefault(); // 기본 링크 동작을 막기

        const currentAttrValue = $(this).attr('href'); // 클릭된 탭의 href 값을 가져옴

        // 탭 컨텐츠 표시/숨기기
        $('.tab-cont').hide(); // 모든 탭 컨텐츠 숨기기
        $(currentAttrValue).show(); // 클릭된 탭의 컨텐츠만 보이기

        // 탭 버튼 활성화/비활성화
        $('.tab-block li').removeClass('on'); // 모든 탭 버튼 비활성화
        $(this).parent('li').addClass('on'); // 클릭된 탭 버튼만 활성화

    });

    function setDateRange(period) {
        const endDate = new Date();
        let startDate = new Date();

        switch (period) {
            case 'D7':
                startDate.setDate(startDate.getDate() - 7);
                break;
            case 'M1':
                startDate.setMonth(startDate.getMonth() - 1);
                break;
            case 'M3':
                startDate.setMonth(startDate.getMonth() - 3);
                break;
            case 'M6':
                startDate.setMonth(startDate.getMonth() - 6);
                break;
            default:
                return;
        }

        // 날짜 형식을 YYYY.MM.DD로 변환
        function formatDate(date) {
            const year = date.getFullYear();
            const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
            const day = date.getDate().toString().padStart(2, '0');

            return `${year}.${month}.${day}`;
        }

        // input에 날짜 범위 설정
        $('#startDate').val(formatDate(startDate));
        $('#endDate').val(formatDate(endDate));
    }

    // 페이지 로드시 현재 선택된 버튼에 대한 날짜 범위 설정
    setDateRange($('.btn-period .btn.on').val());

    // 각 버튼 클릭 이벤트
    $('.btn-period .btn').click(function() {
        $('.btn-period .btn').removeClass('on'); // 모든 버튼의 활성화 상태 해제
        $(this).addClass('on'); // 클릭한 버튼만 활성화

        const period = $(this).val();
        setDateRange(period);
    });

    $("#btnSearch").on("click", function() {
        const startDate = $("#startDate").val();
        const endDate = $("#endDate").val();

        let status = $('input[name="status"]:checked').val();

        $.ajax({
            url: "/mypage/purchase",
            type: 'POST',
            data: {
                startDate: startDate,
                endDate: endDate,
                status: status
            },
            success: function(data) {
                let tableBody = $("#purchaceTableBody");
                tableBody.empty(); // Clear current table content

                $.each(data, function(index, purchase) {
                    tableBody.append(`
                        <tr>
                            <td>${moment(purchase.purchaseDate).format("yyyy-MM-DD")}</td>
                            <td>${purchase.product.name}</td>
                            <td>${purchase.price % 1000 === 0 ? new Intl.NumberFormat('ko-KR').format(purchase.price) : purchase.price }</td>
                            <td>${purchase.status === 'P' ? '구매' : '취소'}</td>
                        </tr>
                    `);
                    // console.log("구매일자 -> ", purchase.purchaseDate);
                    // console.log("상품명 -> ", purchase.product.name);
                    // console.log("가격 -> ", purchase.price);
                    // console.log("상태 -> ", purchase.status);
                });
            },
            error: function(error) {
                console.error("Error:", error);
            }
        });
    });

})

// datepicker 설정
$(() => {
    $(".datepicker").datepicker({
        dateFormat: 'yy-mm-dd' //달력 날짜 형태
        , showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
        , showMonthAfterYear: true // 월- 년 순서가아닌 년도 - 월 순서
        , changeYear: true //option값 년 선택 가능
        , changeMonth: true //option값  월 선택 가능
        , showOn: "button" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시
        , buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
        , buttonImageOnly: true //버튼 이미지만 깔끔하게 보이게함
        , buttonText: "선택" //버튼 호버 텍스트
        , yearSuffix: "년" //달력의 년도 부분 뒤 텍스트
        , monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] //달력의 월 부분 텍스트
        , monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'] //달력의 월 부분 Tooltip
        , dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'] //달력의 요일 텍스트
        , dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'] //달력의 요일 Tooltip
        , yearRange: "-100:+0" // 선택 가능한 년도 범위 (현재부터 100년 전부터 현재까지)
    });
})