$(document).ready(function() {

    $(".btn-order-form").on("click", function(e) {
        e.preventDefault();

        const requestData = [];

        // 각 상품 정보 수집
        $(".product-container").each(function() {
            const productNo = $(this).find("#product-no").val();
            const productAmount = $(this).find("#product-amount").val();
            const productPrice = $(this).find("#product-price").val();

            if (productNo && productAmount && productPrice) {
                requestData.push({
                    productNo: productNo,
                    productAmount: productAmount,
                    productPrice: productPrice
                });
            }
        });

        // 각 패키지 정보 수집
        $(".package-container").each(function() {
            const packageNo = $(this).find("#package-no").val();
            const packageAmount = $(this).find("#package-amount").val();
            const packagePrice = $(this).find("#package-price").val();

            if (packageNo && packagePrice && packageAmount) {
                requestData.push({
                    packageNo: packageNo,
                    packageAmount: packageAmount,
                    packagePrice: packagePrice
                });
            }
        });

        const totalPrice = $("#order-total-price").val();

        $.ajax({
            type: "POST",
            url: "/order/order",
            data: JSON.stringify({requestData, totalPrice}),
            contentType: "application/json",
            success: function success() {
                swal.fire({
                    icon: 'success',
                    text: "구매가 완료되었습니다.",
                    confirmButtonText: '확인'
                }).then((result) => {
                    if (result.value) {
                        window.location.href = "http://localhost/cart";
                    }
                })
            },
            error: function error() {
                Swal.fire({
                    icon: 'error',
                    text: "오류가 발생하였습니다."
                })
                console.log("에러: " + error);
            }
        });
    });

    $(".delete-btn").on("click", function(e) {
        e.preventDefault();

        const cartNo = $(this).attr("data-cart-no");

        $.ajax({
            type: "POST",
            url: "/cart/delete",
            data: {cartNo: cartNo},
            success: function(data) {
                Swal.fire({
                    icon: 'success',
                    text: "상품이 삭제되었습니다.",
                    confirmButtonText: '확인'
                }).then((result) => {
                    if (result.value) {
                        window.location.href = "http://localhost/cart";
                    }
                })
            },
            error: function(error) {
                Swal.fire({
                    icon: 'error',
                    text: "오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.",
                    confirmButtonText: '확인'
                })
                console.log("전달된 값: " + cartNo);
            }
        })
    })
})

function addCommasToPrice() {
    let priceElements = document.querySelectorAll(".discountedPrice");
    priceElements.forEach(function(element) {
        let priceValue = element.textContent;
        let formattedPrice = parseFloat(priceValue).toLocaleString('en-US');
        element.textContent = formattedPrice;
    });
}

window.onload = addCommasToPrice;

