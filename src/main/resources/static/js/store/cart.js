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
                        console.log("삭제된 카트번호: " + cartNo);
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
    let priceElements = document.querySelectorAll(".priceTimesAmount");
    priceElements.forEach(function(element) {
        let priceValue = element.textContent;
        let formattedPrice = parseFloat(priceValue).toLocaleString('en-US');
        element.textContent = formattedPrice;
    });
}

window.onload = addCommasToPrice;
document.addEventListener("DOMContentLoaded", function() {
    // 상품 가격을 저장할 변수 초기화
    let totalPrice = 0;

    // 각 상품의 가격 요소를 선택
    const priceElements = document.querySelectorAll(".priceTimesAmount");

    // 각 상품 가격을 더하여 총 가격 계산
    priceElements.forEach(function(element) {
        const price = parseInt(element.textContent);
        totalPrice += price;
    });

    // 총 가격을 총 합 출력 요소에 넣기
    const totalPriceElement = document.getElementById("totPrdtAmtView");
    if (totalPriceElement) {
        totalPriceElement.textContent = totalPrice.toLocaleString();
    }
});


