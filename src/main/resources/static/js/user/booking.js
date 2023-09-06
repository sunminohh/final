$(() => {

    $('.tab-block a').click(function (e) {
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
    $('.btn-period .btn').click(function () {
        $('.btn-period .btn').removeClass('on');
        $(this).addClass('on');

        const period = $(this).val();
        setDateRange(period);
    });

    $("#btnCheck").on("click", function () {
        searchPurchase();
    });

    // 페이지네이션 클릭
    $('.pagination').on('click', '.page-number-link', function(event) {
        event.preventDefault();
        let page = $(this).attr("data-page");
        $('.page-number-link').removeClass('active');
        $(this).addClass('active');
        searchPurchase(page);
    })

    function searchPurchase(page = 1) {
        const startDate = $("#startDate").val();
        const endDate = $("#endDate").val();
        const status = $('input[name="status"]:checked').val();

        $.ajax({
            url: "/mypage/purchase",
            type: 'POST',
            data: { startDate, endDate, status, page },
            success: function (data) {
                let $tbody = $("#purchaceTableBody");
                let $pagination = $(".pagination");
                let $totalRows = $(".font-gblue");
                const { purchases, pagination, totalRows } = data;

                $totalRows.text(totalRows);
                $tbody.empty();

                if (purchases.length === 0) {
                    $tbody.append(`
                        <tr>
                            <td colspan="4" class="a-c">결제내역이 없습니다.</td>
                        </tr>
                    `);
                    $pagination.empty();
                } else {
                    $.each(purchases, function (index, purchase) {
                        let priceFormatted = purchase.price % 1000 === 0 ? new Intl.NumberFormat('ko-KR').format(purchase.price) : purchase.price;

                        let actionCellContent;
                        let statusClass = '';

                        if (cancelPurchase(purchase.purchaseDate) && purchase.status === 'P') {
                            actionCellContent = `<button type="button" class="button gray-line small btnCancelPruc" data-purchase-no="${purchase.no}">구매취소</button>`;
                            statusClass = 'font-gblue';
                        } else if (purchase.status === 'P') {
                            actionCellContent = `<button type="button" class="button gray-line small btnCancelPruc" data-purchase-no="${purchase.no}">구매취소</button>`;
                            statusClass = 'font-gblue';
                        } else {
                            actionCellContent = '결제취소';
                            statusClass = 'font-red';
                        }

                        $tbody.append(`
                        <tr>
                            <td>${moment(purchase.purchaseDate).format("yyyy-MM-DD")}</td>
                            <td>${purchase.product.name}</td>
                            <td class="${statusClass}">${priceFormatted}</td>
                            <td>${actionCellContent}</td>
                            
                        </tr>
                        `);
                    });

                    $pagination.html(renderPagination(pagination));
                }
            },
            error: function (error) {
                console.error("Error:", error);
            }
        });

    }

    function cancelPurchase(purchaseDate) {
        let currentDate = moment();
        let expirationDate = moment(purchaseDate).add(7, 'days');

        return currentDate.isBefore(expirationDate);
    }

    $(document).on('click', '.btnCancelPruc', function () {
        let purchaseNo = $(this).attr("data-purchase-no");
        let purchaseDate = $(this).closest('tr').find('td:first').text();

        if (!cancelPurchase(purchaseDate)) {
            Swal.fire({
                icon: "warning",
                title: "구매 취소 불가",
                text: "구매 취소 기간이 지났습니다."
            });
            return;
        }

        Swal.fire({
            icon: 'warning',
            text: "구매취소 하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: '확인',
            cancelButtonText: '취소',
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '/mypage/purchase/cancel',
                    type: "POST",
                    data: {"no": purchaseNo},
                    success: function () {
                        Swal.fire({
                            icon: 'success',
                            title: '취소 완료',
                            text: '구매취소 되었습니다.'
                        });
                        searchPurchase();
                    },
                    error: function (error) {
                        Swal.fire({
                            icon: 'error',
                            title: '오류 발생',
                            text: error.responseText
                        })
                    }
                })
            }
        });
    })

    $(document).on('click', '.btnCancelPruc[disabled]', function () {
        Swal.fire({
            icon: "warning",
            title: "구매 취소 불가",
            text: "구매취소 기간이 지났습니다."
        });
    })
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