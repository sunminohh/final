$(function() {

    let productLimit = 10;

    $(".line .cont button").click(function () {
        let btn_name = $(this).attr("class");
        let input_d = $(".line .cont input[type='text']");

        let input_num = Number(input_d.val());
        if (btn_name == "btn minus") {
            if (input_num > 1) {
                input_d.val(input_num - 1);
            }
        } else if (btn_name == "btn plus") {
            if (input_num < productLimit) {
                input_d.val(input_num + 1);
            } else if (Number(productLimit) == -1) {
                input_d.val(input_num + 1);
            }

        }
        updateTotalAmount();

    })
    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    }
    function updateTotalAmount() {
        let input_d = $(".line .cont input[type='text']");
        let input_num = Number(input_d.val());
        let price = Number($('#price').val());
        let totalAmount = price * input_num;

        $('#prdtSumAmt').html(numberWithCommas(totalAmount));
        $('#totalPrice').val(totalAmount);
        $('#packageAmount').val(input_num);

    }

    updateTotalAmount();

    // 구매 후 취소 및 상품 이용 안내


    $(".box-pulldown").on("click", function() {
        if ($(this).hasClass("on")) {
            $(this).removeClass("on");
        } else {
            $(this).addClass("on");
        }
    });

    $(document).ready(function() {
        $('#btnCart-user').click(function(event) {
            event.preventDefault();

            const totalPrice = $("#totalPrice").val();
            const userId = $("#userId").val();
            const packageNo = $("#packageNo").val();
            const packageAmount = $("#packageAmount").val();

            const requestData = {
                totalPrice: totalPrice,
                userId: userId,
                packageNo: packageNo,
                packageAmount: packageAmount
            };

            $.ajax({
                type: "POST",
                url: "/cart/addPackageIntoCart",
                data: requestData,
                success: function success() {
                    Swal.fire({
                        icon: 'success',
                        text: "상품이 장바구니에 담겼습니다."
                    })
                    console.log("정보가 성공적으로 서버에 전달되었습니다.");

                    // 페이지 리다이렉션
                },
                error: function error() {
                    Swal.fire({
                        icon: 'error',
                        text: "오류가 발생하였습니다. 잠시 후 다시 시도해 주세요."
                    })
                    console.log("정보가 서버에 전달되지 않았습니다.");
                }
            })
        })
    })

    const $username = $("input[name='username']");

    $(".btnCart-not-user").on("click", function (e) {
        e.preventDefault();
        Swal.fire({
            icon: 'warning',
            title: "이용 제한",
            text: "로그인 후 이용가능합니다. 로그인 하시겠습니까?",
            showCancelButton: true,
            confirmButtonText: '네',
            cancelButtonText: '아니오',
            didClose: () => {
                $username.focus();
            }
        }).then((result) => {
            if (result.isConfirmed) {
                $(".btn-login").trigger("click");
            }
        });
    });
})
