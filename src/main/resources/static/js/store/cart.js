$(document).ready(function() {

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
