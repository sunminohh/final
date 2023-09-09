$(function() {

    let productLimit = 10;

    let amount;

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
        let discountedPrice = Number($('#discountedPrice').val());
        let originalPrice = Number($('#originalPrice').val());

        amount = discountedPrice * input_num;
        let totalOriginalPrice = originalPrice * input_num;

        $('#prdtSumAmt').html(numberWithCommas(amount));

        $('#totalOriginalPrice').val(totalOriginalPrice);
        $('#totalDiscountedPrice').val(amount);
        $('#packageAmount').val(input_num);

    }

    updateTotalAmount();

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

            const totalDiscountedPrice = $("#totalDiscountedPrice").val();
            const totalOriginalPrice = $("#totalOriginalPrice").val();
            const userId = $("#userId").val();
            const packageNo = $("#packageNo").val();
            const packageAmount = $("#packageAmount").val();
            const catNo = $("#catNo").val();

            const requestData = {
                totalDiscountedPrice: totalDiscountedPrice,
                totalOriginalPrice: totalOriginalPrice,
                userId: userId,
                packageNo: packageNo,
                packageAmount: packageAmount,
                catNo: catNo
            };

            $.ajax({
                type: "POST",
                url: "/cart/addPackageIntoCart",
                data: requestData,
                success: function success() {
                    Swal.fire({
                        icon: 'success',
                        text: "상품이 장바구니에 담겼습니다.",
                        confirmButtonText: '확인'
                    }).then((result) => {
                        if (result.value) {
                            console.log("정보가 성공적으로 서버에 전달되었습니다.");
                            window.location.href = "http://localhost/store";
                        }
                    })
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

    const orderNameInput = document.getElementById("package-name")



        const bundle=$(".bundle").filter(":first")
        const bundleText=bundle.text()
        let a=parseInt(bundleText.charAt(bundleText.indexOf('일반 관람권')+7))

    let giftTickets = a>0? a : 1


    let tossPayments = TossPayments("test_ck_Lex6BJGQOVDY7zZDAQOrW4w2zNbg");

    let orderName = orderNameInput.value;

    let path = "/order/";
    let successUrl = window.location.origin + path + "success";
    let failUrl = window.location.origin + path + "fail";
    let callbackUrl = window.location.origin + path + "va_callback";
    let orderId = new Date().getTime();

    $("#btn-tosspay").click(() => {

        const totalDiscountedPrice = $("#totalDiscountedPrice").val();
        const totalOriginalPrice = $("#totalOriginalPrice").val();
        const userId = $("#userId").val();
        const packageNo = $("#packageNo").val();
        const packageAmount = $("#packageAmount").val();
        const catNo = $("#catNo").val();

        const requestData = {
            totalDiscountedPrice: totalDiscountedPrice,
            totalOriginalPrice: totalOriginalPrice,
            userId: userId,
            packageNo: packageNo,
            packageAmount: packageAmount,
            catNo: catNo,
            orderId: orderId
        };

        $.ajax({
            type: "POST",
            url: "/order/success",
            data: requestData
        })

        let jsons = {
            "card": {
                "amount": amount,
                "orderId": orderId,
                "orderName": giftTickets+" "+orderName,
                "successUrl": successUrl,
                "failUrl": failUrl,
                "cardCompany": null,
                "cardInstallmentPlan": null,
                "maxCardInstallmentPlan": null,
                "useCardPoint": false,
                "customerName": "박토스",
                "customerEmail": null,
                "customerMobilePhone": null,
                "useInternationalCardOnly": false,
                "flowMode": "DEFAULT",
                "discountCode": null,
                "appScheme": null
            }
        }
        console.log(jsons.card)
        pay('카드', jsons.card);

    })


    function pay(method, requestJson) {
        console.log(requestJson);
        tossPayments.requestPayment(method, requestJson)
            .catch(function (error) {

                if (error.code === "USER_CANCEL") {
                    Swal.fire({
                        icon: 'warning',
                        text: "사용자가 취소했습니다."
                    });
                } else {
                    alert(error.message);
                    Swal.fire({
                        icon: 'error',
                        text: error.message
                    });
                }

            });
    }
})
