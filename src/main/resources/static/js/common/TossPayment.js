$(() => {
    const amountInput = document.getElementById("order-total-price")

    let amount = amountInput.value;

    let tossPayments = TossPayments("test_ck_Lex6BJGQOVDY7zZDAQOrW4w2zNbg");


    // 상품명과 패키지명을 저장할 배열을 만듬
    const productNames = [];
    const packageNames = [];
    let giftTickets = 0


    $(".cart-item").each(function(i,e){
        const bundle=$(this).find(".bundle")
        const s=bundle.text()
        console.log(s)
        const q=parseInt($(this).find(".cart-quantity").text())
        if(s.includes('일반 관람권')){
                let a=parseInt(s.charAt(s.indexOf('일반 관람권')+7))
            giftTickets+= a>0? a*q : q
            }

    })

    // 각 상품과 패키지의 이름을 가져와서 각 배열에 추가
    const productNameElements = document.querySelectorAll(".product-name");
    const packageNameElements = document.querySelectorAll(".package-name");

    productNameElements.forEach(function(element) {
        const productName = element.textContent;
        productNames.push(productName);
    });

    packageNameElements.forEach(function(element) {

        const packageName = element.textContent;
        packageNames.push(packageName);
    });

    console.log("productNames 안에 들어있는 값: ", productNames);
    console.log("packageNames 안에 들어있는 값: ", packageNames);

    // productNames와 packageNames 배열을 결합하여 모든 상품과 패키지의 이름을 가져옴
    const allProductNames = productNames.concat(packageNames);

    console.log("allProductNames 안에 들어있는 값: ", allProductNames);

    // orderName을 생성
    let orderName = "";
    if (allProductNames.length > 0) {
        if (allProductNames.length === 1) {
            orderName = allProductNames[0];
        } else {
            const otherProductsCount = allProductNames.length - 1;
            orderName = allProductNames[0] + " 외 " + otherProductsCount + " 건";
        }
    } else {
        orderName = "주문 상품 없음"; // 상품이 없는 경우에 대한 예외 처리
    }

    console.log("orderName 안에 있는 값: ", orderName);

    let path = "/order/";
    let successUrl = window.location.origin + path + "success";
    let failUrl = window.location.origin + path + "fail";
    let callbackUrl = window.location.origin + path + "va_callback";
    let orderId = new Date().getTime();
    let uuid = self.crypto.randomUUID();

    let jsons = {
        "card": {
            "amount": amount,
            "orderId": uuid + orderId,
            "orderName": giftTickets +" "+orderName,
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

    console.log("jsons.card 값: ", jsons.card)

    $("#btn-tosspay").click(() => {
        pay('카드', jsons.card);
    })

    function pay(method, requestJson) {
        console.log(requestJson);
        tossPayments.requestPayment(method, requestJson)
            .then(function(response) {
                if (response.code === "SUCCESS") {

                    Swal.fire({
                        icon: 'success',
                        text: "구매가 완료되었습니다."
                    })
                }
            })
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